package org.brapi.test.SampleOrchestratorServer.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.brapi.test.SampleOrchestratorServer.controller.SocketController;
import org.brapi.test.SampleOrchestratorServer.model.entity.PlateEntity;
import org.brapi.test.SampleOrchestratorServer.model.entity.PlatePositionEntity;
import org.brapi.test.SampleOrchestratorServer.model.entity.SampleEntity;
import org.brapi.test.SampleOrchestratorServer.model.entity.SubmissionEntity;
import org.brapi.test.SampleOrchestratorServer.model.entity.SubmissionStageEnum;
import org.brapi.test.SampleOrchestratorServer.model.json.Measurement;
import org.brapi.test.SampleOrchestratorServer.model.json.PlateFormat;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorOrderRequest;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorOrderRequest.SampleTypeEnum;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorOrderRequestPlates;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorOrderResponse;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorOrderStatusResponse;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorOrderStatusResponseResult.StatusEnum;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorSample;
import org.brapi.test.SampleOrchestratorServer.repository.SubmissionRepository;
import org.brapi.test.SampleOrchestratorServer.service.vendor.VendorServiceFactory;
import org.brapi.test.SampleOrchestratorServer.service.vendor.VendorServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SampleSubmissionService {
	private static Logger logger = LoggerFactory.getLogger(SampleSubmissionService.class);

	private RestClientService restClientService;
	private SubmissionRepository submissionRepository;
	private VendorServiceFactory vendorServiceFactory;
	private SocketController socketController;

	@Autowired
	public SampleSubmissionService(RestClientService restClientService, SubmissionRepository submissionRepository,
			VendorServiceFactory vendorServiceFactory, SocketController socketController) {
		this.restClientService = restClientService;
		this.submissionRepository = submissionRepository;
		this.vendorServiceFactory = vendorServiceFactory;
		this.socketController = socketController;
	}

	@Async
	public void submitSamples(String submissionId) {
		Optional<SubmissionEntity> submissionOpt = submissionRepository.findById(submissionId);

		if (submissionOpt.isPresent()) {
			SubmissionEntity submission = submissionOpt.get();
			
			socketController.pushSubmissionStatus(submission.getId());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				VendorServiceInterface uniqueVendorService = vendorServiceFactory
						.getVendorService(submission.getVendor().getVendorServiceClass());
				logger.info(submission.getShortId() + " Sending sample data to Vendor");

				VendorOrderRequest request = new VendorOrderRequest();
				request.setClientId(submission.getId());
				request.setNumberOfSamples(submission.getSampleGroup().getSamples().size());
				request.setPlates(convertPlates(submission.getSampleGroup().getPlates()));
				request.setRequiredServiceInfo(uniqueVendorService.getRequiredServiceInfo());
				request.setSampleType(SampleTypeEnum.TISSUE);
				request.setServiceIds(Arrays.asList(submission.getVendor_ServiceId()));

				VendorOrderResponse response = restClientService.postVendorOrders(
						submission.getVendor().getVendorBaseURL(), uniqueVendorService.getAuthToken(), request);

				if (response != null) {
					submission.setVendor_OrderId(response.getResult().getOrderId());
					submission.setSubmissionStage(SubmissionStageEnum.WAITING_FOR_VENDOR);
					submission.setVendor_PollingAttempts(0);
					logger.info("\t" + submission.getShortId() + " Vendor accepted sample data with Vendor Order Id "
							+ response.getResult().getOrderId());
				} else {
					submission.setSubmissionStage(SubmissionStageEnum.ERROR);
					submission.setVendor_PollingAttempts(0);
					submission.setErrorMsg("Submission to vendor failed");
					logger.error("\t" + submission.getShortId() + " Submission to vendor failed");
				}

			} catch (Exception e) {
				submission.setSubmissionStage(SubmissionStageEnum.ERROR);
				submission.setErrorMsg("Exception Occured");
				logger.error("\t" + submission.getShortId() + " Exception Occured");
			} finally {
				submissionRepository.save(submission);
				socketController.pushSubmissionStatus(submission.getId());
			}
		} else {
			logger.error("no submission found for ID " + submissionId);
		}

	}

	@Scheduled(fixedDelay = 10000)
	public void pollVendorSubmissions() {
		logger.debug("Polling Vendor Submissions");
		Pageable pageReq = PageRequest.of(0, 1000, Sort.unsorted());
		Page<SubmissionEntity> submissions = submissionRepository
				.findBySubmissionStage(SubmissionStageEnum.WAITING_FOR_VENDOR, pageReq);
		for (SubmissionEntity submission : submissions) {
			try {
				String authToken = vendorServiceFactory.getVendorService(submission.getVendor().getVendorServiceClass())
						.getAuthToken();
				VendorOrderStatusResponse status = restClientService.getVendorOrdersStatus(
						submission.getVendor().getVendorBaseURL(), authToken, submission.getVendor_OrderId());
				submission.setVendor_LastStatus(status.getResult().getStatus());
				submission.setVendor_PollingAttempts(submission.getVendor_PollingAttempts() + 1);

				logger.info("\t" + submission.getShortId() + " Vendor Order Id: " + submission.getVendor_OrderId());
				logger.info("\t" + submission.getShortId() + " Vendor status: " + submission.getVendor_LastStatus());
				logger.info("\t" + submission.getShortId() + " Vendor polling attempt: "
						+ submission.getVendor_PollingAttempts());

				if (status.getResult().getStatus() == StatusEnum.COMPLETED) {
					submission.setSubmissionStage(SubmissionStageEnum.SENT_TO_GENOTYPE_DB);
					logger.info("\t" + submission.getShortId() + " Vendor has finished!");
				}
				if (status.getResult().getStatus() == StatusEnum.REJECTED) {
					submission.setSubmissionStage(SubmissionStageEnum.ERROR);
					logger.info("\t" + submission.getShortId() + " Vendor has rejected!");
					logger.info("\t" + submission.getShortId() + " " + submission.getErrorMsg());
				}
				if (submission.getVendor_PollingAttempts() >= 5) {
					submission.setSubmissionStage(SubmissionStageEnum.ERROR);
					submission.setErrorMsg("Vendor submission has exceeded maximum polling attempts");
					logger.error("\t" + submission.getShortId()
							+ " Vendor submission has exceeded maximum polling attempts");
				}

			} catch (Exception e) {
				submission.setSubmissionStage(SubmissionStageEnum.ERROR);
				submission.setErrorMsg("Exception Occured");
				logger.error("\t" + submission.getShortId() + " Exception Occured");
			} finally {
				submissionRepository.save(submission);
				socketController.pushSubmissionStatus(submission.getId());

				if (submission.getSubmissionStage() == SubmissionStageEnum.SENT_TO_GENOTYPE_DB) {
					// Start Async thread after db has been updated
					sendToGenotypeDB(submission.getId());
				}
			}
		}
	}

	@Async
	public void sendToGenotypeDB(String submissionId) {
		Optional<SubmissionEntity> submissionOpt = submissionRepository.findById(submissionId);

		if (submissionOpt.isPresent()) {
			SubmissionEntity submission = submissionOpt.get();
			try {

				// GOBii load sequence goes here
				submission.setGenotypeDB_LastStatus("loading");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				
				
				logger.info(submission.getShortId() + " Results are being sent to the Genotyping Database");
				submission.setSubmissionStage(SubmissionStageEnum.WAITING_FOR_GENOTYPE_DATABASE);
				submission.setGenotypeDB_PollingAttempts(0);
			} catch (Exception e) {
				submission.setSubmissionStage(SubmissionStageEnum.ERROR);
				submission.setErrorMsg("Exception Occured");
				logger.error("\t" + submission.getShortId() + " Exception Occured");
			} finally {
				submissionRepository.save(submission);
				socketController.pushSubmissionStatus(submission.getId());
			}
		}
	}

	@Scheduled(fixedDelay = 10000)
	public void pollGenotypeDB() {
		logger.debug("Polling Genotyping DB Storage Status");
		Pageable pageReq = PageRequest.of(0, 1000, Sort.unsorted());
		Page<SubmissionEntity> submissions = submissionRepository
				.findBySubmissionStage(SubmissionStageEnum.WAITING_FOR_GENOTYPE_DATABASE, pageReq);
		for (SubmissionEntity submission : submissions) {
			try {
				String status = restClientService.getGenotypeDBLoadStatus(submission.getGenotypeDB_OrderId());
				submission.setGenotypeDB_LastStatus(status);
				submission.setGenotypeDB_PollingAttempts(submission.getGenotypeDB_PollingAttempts() + 1);

				logger.info(
						"\t" + submission.getShortId() + " Genotype DB Load Id: " + submission.getGenotypeDB_OrderId());
				logger.info("\t" + submission.getShortId() + " Genotype DB status: "
						+ submission.getGenotypeDB_LastStatus());
				logger.info("\t" + submission.getShortId() + " Genotype DB polling attempt: "
						+ submission.getGenotypeDB_PollingAttempts());

				if (status == StatusEnum.COMPLETED.toString()) {
					submission.setSubmissionStage(SubmissionStageEnum.COMPLETE);
					logger.info("\t" + submission.getShortId() + " Genotype data loaded successfully!");
				}
				if (status == StatusEnum.REJECTED.toString()) {
					submission.setSubmissionStage(SubmissionStageEnum.ERROR);
					logger.info("\t" + submission.getShortId() + " Genotype data load has failed!");
					logger.info("\t" + submission.getShortId() + " " + submission.getErrorMsg());
				}
				if (submission.getGenotypeDB_PollingAttempts() >= 5) {
					submission.setSubmissionStage(SubmissionStageEnum.ERROR);
					submission.setErrorMsg("Genotype data load has exceeded maximum polling attempts");
					logger.error("\t" + submission.getShortId()
							+ " Genotype data load has exceeded maximum polling attempts");
				}
				submissionRepository.save(submission);
				socketController.pushSubmissionStatus(submission.getId());

			} catch (Exception e) {
				submission.setSubmissionStage(SubmissionStageEnum.ERROR);
				submission.setErrorMsg("Exception Occured");
				logger.error("\t" + submission.getShortId() + " Exception Occured");
				socketController.pushSubmissionStatus(submission.getId());
			}
		}
	}

	private List<VendorOrderRequestPlates> convertPlates(Set<PlateEntity> plateEntities) {
		List<VendorOrderRequestPlates> plates = new ArrayList<>();
		for (PlateEntity plateEntity : plateEntities) {
			VendorOrderRequestPlates plate = new VendorOrderRequestPlates();
			plate.setClientPlateBarcode(plateEntity.getId());
			plate.setClientPlateId(plateEntity.getId());
			List<VendorSample> samples = new ArrayList<>();
			for (PlatePositionEntity platePos : plateEntity.getSamples()) {
				SampleEntity sampleEntity = platePos.getSample();
				VendorSample sample = convertSample(sampleEntity, platePos);
				samples.add(sample);
			}
			plate.setSamples(samples);
			plate.setSampleSubmissionFormat(PlateFormat.PLATE_96);
			plates.add(plate);
		}
		return plates;
	}

	private VendorSample convertSample(SampleEntity entity, PlatePositionEntity platePos) {
		VendorSample sample = new VendorSample();
		sample.setClientSampleBarCode(entity.getId());
		sample.setClientSampleId(entity.getId());
		sample.setColumn(platePos.getPosition().name());
		sample.setComments(entity.getNotes());
		Measurement con = new Measurement().units("ml").value(new BigDecimal(entity.getConcentration()));
		sample.setConcentration(con);
		sample.setOrganismName(null);
		sample.setRow(platePos.getPosition().name());
		sample.setSpeciesName(null);
		sample.setTaxonomyOntologyReference(null);
		sample.setTissueType(entity.getTissueType());
		sample.setTissueTypeOntologyReference(null);
		Measurement vol = new Measurement().units("ml").value(new BigDecimal(entity.getVolume()));
		sample.setVolume(vol);
		sample.setWell(platePos.getPosition().name());
		return sample;
	}
}
