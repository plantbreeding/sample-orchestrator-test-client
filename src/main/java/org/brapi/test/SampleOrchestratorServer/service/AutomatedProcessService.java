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
import org.brapi.test.SampleOrchestratorServer.model.json.VendorOntologyReference;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorOrderDetails;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorOrderDetailsResponse;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorOrderRequest;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorOrderRequest.SampleTypeEnum;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorOrderRequestPlates;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorOrderResponse;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorOrderResultsResponse;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorOrderStatusResponse;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorOrderStatusResponseResult.StatusEnum;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorSample;
import org.brapi.test.SampleOrchestratorServer.repository.SubmissionRepository;
import org.brapi.test.SampleOrchestratorServer.service.genotyping.GenotypeDBServiceFactory;
import org.brapi.test.SampleOrchestratorServer.service.genotyping.GenotypeDBServiceInterface;
import org.brapi.test.SampleOrchestratorServer.service.vendor.VendorServiceFactory;
import org.brapi.test.SampleOrchestratorServer.service.vendor.VendorServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AutomatedProcessService {
	private static Logger logger = LoggerFactory.getLogger(AutomatedProcessService.class);

	@Value("${vendor.maxPollingAttempts}")
	private Integer vendorMaxPollingAttempts;
	@Value("${genotype.maxPollingAttempts}")
	private Integer genotypeMaxPollingAttempts;

	private RestClientService restClientService;
	private SubmissionRepository submissionRepository;
	private VendorServiceFactory vendorServiceFactory;
	private GenotypeDBServiceFactory genotypeDBServiceFactory;
	private SocketController socketController;

	@Autowired
	public AutomatedProcessService(RestClientService restClientService, SubmissionRepository submissionRepository,
			VendorServiceFactory vendorServiceFactory, GenotypeDBServiceFactory genotypeDBServiceFactory, SocketController socketController) {
		this.restClientService = restClientService;
		this.submissionRepository = submissionRepository;
		this.vendorServiceFactory = vendorServiceFactory;
		this.genotypeDBServiceFactory = genotypeDBServiceFactory;
		this.socketController = socketController;
	}

	@Async
	public void submitSamples(String submissionId) {
		Optional<SubmissionEntity> submissionOpt = submissionRepository.findById(submissionId);

		if (submissionOpt.isPresent()) {
			SubmissionEntity submission = submissionOpt.get();

			socketController.pushSubmissionStatus(submission.getId());
			logger.info(submission.getShortId() + " Sending sample data to Vendor");

			try {
				VendorServiceInterface vendorService = vendorServiceFactory
						.getVendorService(submission.getVendor().getVendorServiceClass());
				
				if (vendorService.isFullOrder()) {
					submitFullOrder(submission, vendorService);
				} else {
					submitSamplesOnly(submission, vendorService);
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

	private void submitSamplesOnly(SubmissionEntity submission, VendorServiceInterface vendorService) {
		VendorOrderRequest request = new VendorOrderRequest();
		request.setClientId(vendorService.getClientId());
		request.setNumberOfSamples(submission.getSampleGroup().getSamples().size());
		request.setPlates(convertPlates(submission));
		request.setSampleType(SampleTypeEnum.TISSUE);

		VendorOrderResponse response = restClientService.postVendorSamples(submission.getVendor().getVendorBaseURL(),
				vendorService.getAuthToken(), request);

		if (response != null) {
			submission.setVendor_SamplesId(response.getResult().getSubmissionId());
			submission.setSubmissionStage(SubmissionStageEnum.WAITING_FOR_USER);
			submission.setErrorMsg("Please click <a href=\""
					+ submission.getVendor().getVendorBaseURL().replace("/brapi/v1", "/order.pl")
					+ "\" target=\"_blank\">HERE</a> to complete order. Submission Id: "
					+ response.getResult().getSubmissionId());
			submission.setVendor_PollingAttempts(0);
			logger.info("\t" + submission.getShortId() + " Vendor accepted sample data with Vendor Order Id "
					+ response.getResult().getOrderId());
		} else {
			submission.setSubmissionStage(SubmissionStageEnum.ERROR);
			submission.setVendor_PollingAttempts(0);
			submission.setErrorMsg("Submission to vendor failed");
			logger.error("\t" + submission.getShortId() + " Submission to vendor failed");
		}
	}

	private void submitFullOrder(SubmissionEntity submission, VendorServiceInterface vendorService) {
		VendorOrderRequest request = new VendorOrderRequest();
		request.setClientId(vendorService.getClientId());
		request.setNumberOfSamples(submission.getSampleGroup().getSamples().size());
		request.setPlates(convertPlates(submission));
		request.setRequiredServiceInfo(vendorService.getRequiredServiceInfo());
		request.setSampleType(SampleTypeEnum.TISSUE);
		request.setServiceIds(Arrays.asList(submission.getVendorService().getServiceId()));

		VendorOrderResponse response = restClientService.postVendorOrders(submission.getVendor().getVendorBaseURL(),
				vendorService.getAuthToken(), request);

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

	}

	@Scheduled(fixedDelay = 10000)
	public void pollVendorSubmissions() {
		logger.debug("Polling Vendor Submissions");
		Pageable pageReq = PageRequest.of(0, 1000, Sort.unsorted());
		Page<SubmissionEntity> submissions = submissionRepository
				.findBySubmissionStage(SubmissionStageEnum.WAITING_FOR_USER, pageReq);
		for (SubmissionEntity submission : submissions) {
			try {
				String authToken = vendorServiceFactory.getVendorService(submission.getVendor().getVendorServiceClass())
						.getAuthToken();
				VendorOrderDetailsResponse response = restClientService.getVendorOrdersForSubmission(
						submission.getVendor().getVendorBaseURL(), authToken, submission.getVendor_SamplesId());

				submission.setVendor_PollingAttempts(submission.getVendor_PollingAttempts() + 1);

				logger.info("\t" + submission.getShortId() + " Vendor Samples Id: " + submission.getVendor_SamplesId());
				logger.info("\t" + submission.getShortId() + " Vendor status: " + submission.getVendor_LastStatus());
				logger.info("\t" + submission.getShortId() + " Vendor polling attempt: "
						+ submission.getVendor_PollingAttempts());

				if (!response.getResult().getData().isEmpty()) {
					VendorOrderDetails order = response.getResult().getData().get(0);
					submission.setVendor_OrderId(order.getOrderId());
					submission.setSubmissionStage(SubmissionStageEnum.WAITING_FOR_VENDOR);
					submission.setVendor_PollingAttempts(0);
					logger.info("\t" + submission.getShortId() + " Vendor accepted sample data with Vendor Order Id "
							+ order.getOrderId());
				}
				if (submission.getVendor_PollingAttempts() >= vendorMaxPollingAttempts) {
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
			}
		}
	}

	@Scheduled(fixedDelay = 10000)
	public void pollVendorOrders() {
		logger.debug("Polling Vendor Orders");
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

					VendorOrderResultsResponse results = restClientService.getVendorOrdersResults(
							submission.getVendor().getVendorBaseURL(), authToken, submission.getVendor_OrderId());
					
					submission.setSubmissionStage(SubmissionStageEnum.SENT_TO_GENOTYPE_DB);
					logger.info("\t" + submission.getShortId() + " Vendor has finished!");
				}
				if (status.getResult().getStatus() == StatusEnum.REJECTED) {
					submission.setSubmissionStage(SubmissionStageEnum.ERROR);
					submission.setErrorMsg("Vendor has rejected the submission");
					logger.info("\t" + submission.getShortId() + " Vendor has rejected");
					logger.info("\t" + submission.getShortId() + " " + submission.getErrorMsg());
				}
				if (submission.getVendor_PollingAttempts() >= vendorMaxPollingAttempts) {
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
				
				GenotypeDBServiceInterface genotypeDbService = genotypeDBServiceFactory
						.getGenotypeDbService(submission.getGenotypeDB().getGenotypeDbServiceClass());
				
				genotypeDbService.preUploadSetup();
				genotypeDbService.sendSampleData();
				genotypeDbService.sendDataMatrix();

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
				GenotypeDBServiceInterface genotypeDBService = genotypeDBServiceFactory.
						getGenotypeDbService(submission.getGenotypeDB().getGenotypeDbServiceClass());
				String status = restClientService.getGenotypeDBLoadStatus(
						submission.getGenotypeDB().getGenotypeDbBaseURL(), 
						genotypeDBService.getAuthToken(), submission.getGenotypeDB_OrderId());
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
				if (submission.getGenotypeDB_PollingAttempts() >= genotypeMaxPollingAttempts) {
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

	private List<VendorOrderRequestPlates> convertPlates(SubmissionEntity submission) {
		List<VendorOrderRequestPlates> plates = new ArrayList<>();
		Set<PlateEntity> plateEntities = submission.getSampleGroup().getPlates();
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
		sample.setColumn(platePos.getPosition().getColumn());
		sample.setComments(entity.getNotes());
		Measurement con = new Measurement().units("ml").value(new BigDecimal(entity.getConcentration()));
		sample.setConcentration(con);
		sample.setOrganismName("Acacia");
		sample.setRow(platePos.getPosition().getRow());
		sample.setSpeciesName("Acacia aemula");
		sample.setTaxonomyOntologyReference(new VendorOntologyReference().ontologyID("4577"));
		sample.setTissueType(entity.getTissueType());
		sample.setTissueTypeOntologyReference(new VendorOntologyReference().ontologyID("0025034"));
		Measurement vol = new Measurement().units("ml").value(new BigDecimal(entity.getVolume()));
		sample.setVolume(vol);
		sample.setWell(platePos.getPosition().name());
		return sample;
	}
}
