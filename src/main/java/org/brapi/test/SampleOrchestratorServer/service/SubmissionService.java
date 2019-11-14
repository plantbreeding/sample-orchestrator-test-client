package org.brapi.test.SampleOrchestratorServer.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.brapi.test.SampleOrchestratorServer.controller.SocketController;
import org.brapi.test.SampleOrchestratorServer.exceptions.BrAPIServerException;
import org.brapi.test.SampleOrchestratorServer.model.entity.GenotypeDBEntity;
import org.brapi.test.SampleOrchestratorServer.model.entity.PlateEntity;
import org.brapi.test.SampleOrchestratorServer.model.entity.PlatePositionEntity;
import org.brapi.test.SampleOrchestratorServer.model.entity.PlatePositionEnum;
import org.brapi.test.SampleOrchestratorServer.model.entity.SampleEntity;
import org.brapi.test.SampleOrchestratorServer.model.entity.SampleGroupEntity;
import org.brapi.test.SampleOrchestratorServer.model.entity.SubmissionEntity;
import org.brapi.test.SampleOrchestratorServer.model.entity.SubmissionStageEnum;
import org.brapi.test.SampleOrchestratorServer.model.entity.VendorEntity;
import org.brapi.test.SampleOrchestratorServer.model.entity.VendorSpecificationServiceEntity;
import org.brapi.test.SampleOrchestratorServer.model.json.Sample;
import org.brapi.test.SampleOrchestratorServer.model.json.SubmissionRequest;
import org.brapi.test.SampleOrchestratorServer.repository.GenotypeDBRepositoy;
import org.brapi.test.SampleOrchestratorServer.repository.PlateRepository;
import org.brapi.test.SampleOrchestratorServer.repository.SampleGroupRepository;
import org.brapi.test.SampleOrchestratorServer.repository.SubmissionRepository;
import org.brapi.test.SampleOrchestratorServer.repository.VendorRepositoy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubmissionService {
	private RestClientService restClientService;

	private VendorRepositoy vendorRepositoy;
	private GenotypeDBRepositoy genotypeDBRepositoy;
	private SampleGroupRepository sampleGroupRepository;
	private SubmissionRepository submissionRepository;

	@Autowired
	public SubmissionService(RestClientService restClientService, VendorRepositoy vendorRepositoy,
			GenotypeDBRepositoy genotypeDBRepositoy, SampleGroupRepository sampleGroupRepository,
			SubmissionRepository submissionRepository, SocketController socketController,
			PlateRepository plateRepository) {

		this.restClientService = restClientService;

		this.vendorRepositoy = vendorRepositoy;
		this.genotypeDBRepositoy = genotypeDBRepositoy;
		this.sampleGroupRepository = sampleGroupRepository;
		this.submissionRepository = submissionRepository;
	}

	public SubmissionEntity buildSubmission(SubmissionRequest request) throws BrAPIServerException {
		SubmissionEntity submission = new SubmissionEntity();
		Optional<VendorEntity> vendorOpt = vendorRepositoy.findById(request.getVendorDbId());
		Optional<VendorSpecificationServiceEntity> serviceOpt = findService(request);
		Optional<GenotypeDBEntity> genotypeDBOpt = genotypeDBRepositoy.findById(request.getGenotypeDbId());

		if (serviceOpt.isPresent() && vendorOpt.isPresent() && genotypeDBOpt.isPresent()) {
			List<Sample> samples = restClientService.getExternalSourceSamples(RestClientService.BASE_URI, "Bearer XXXX").getResult().getData();
			
			SampleGroupEntity sampleGroup = new SampleGroupEntity();
			sampleGroup.setPlates(buildPlates(samples, serviceOpt.get(), sampleGroup));
			sampleGroup = sampleGroupRepository.save(sampleGroup);
			
			submission.setSampleGroup(sampleGroup);
			submission.setVendorService(serviceOpt.get());
			submission.setSubmissionStage(SubmissionStageEnum.NEW_SUBMISSION);
			submission.setVendor(vendorOpt.get());
			submission.setGenotypeDB(genotypeDBOpt.get());
			submission = submissionRepository.save(submission);
		}
		return submission;
	}

	private Set<PlateEntity> buildPlates(List<Sample> samples, VendorSpecificationServiceEntity service,
			SampleGroupEntity sampleGroup) {
		Set<PlateEntity> plateEntities = new HashSet<>();
		PlateEntity plate = new PlateEntity();

		Iterator<PlatePositionEnum> positionsIter = Arrays.asList(PlatePositionEnum.values()).iterator();
		Iterator<Sample> sampleIter = samples.iterator();

		while (sampleIter.hasNext()) {
			PlatePositionEnum pos = findNextPosition(service, positionsIter);
			if (pos != null) {
				PlatePositionEntity platePostion = new PlatePositionEntity();
				platePostion.setPosition(pos);
				SampleEntity sampleEntity = buildSample(sampleIter.next(), sampleGroup);
				sampleGroup.addSample(sampleEntity);
				platePostion.setSample(sampleEntity);
				platePostion.setPlate(plate);
				plate.addSample(platePostion);
				plate.setGroup(sampleGroup);
				plate.setHorizontal();

			} else {
				plateEntities.add(plate);
				plate = new PlateEntity();
				positionsIter = Arrays.asList(PlatePositionEnum.values()).iterator();
			}
		}
		// Add the last plate after running out of samples
		plateEntities.add(plate);
		return plateEntities;
	}

	private SampleEntity buildSample(Sample sample, SampleGroupEntity group) {
		SampleEntity sampleEntity = new SampleEntity();
		sampleEntity.setConcentration("10");
		sampleEntity.setGroup(group);
		sampleEntity.setNotes(sample.getNotes());
		sampleEntity.setSampleExternalId(sample.getSampleExternalId());
		sampleEntity.setSampleTimestamp(DateUtility.toDate(sample.getSampleTimestamp()));
		sampleEntity.setSampleType(sample.getSampleType());
		sampleEntity.setTakenBy(sample.getTakenBy());
		sampleEntity.setTissueType(sample.getTissueType().toLowerCase());
		sampleEntity.setVolume("10");

		return sampleEntity;
	}

	private PlatePositionEnum findNextPosition(VendorSpecificationServiceEntity service,
			Iterator<PlatePositionEnum> positionsIter) {
		PlatePositionEnum pos = null;
		if (positionsIter.hasNext()) {
			pos = positionsIter.next();
			while (isBlank(service, pos)) {
				if (positionsIter.hasNext()) {
					pos = positionsIter.next();
				} else {
					pos = null;
					break;
				}

			}
		}
		return pos;
	}

	private Optional<VendorSpecificationServiceEntity> findService(SubmissionRequest request) {
		Optional<VendorSpecificationServiceEntity> service = Optional.empty();
		Optional<VendorEntity> vendorOpt = vendorRepositoy.findById(request.getVendorDbId());
		if (vendorOpt.isPresent()) {
			for (VendorSpecificationServiceEntity p : vendorOpt.get().getVendorSpec().getServices()) {
				if (p.getId().equalsIgnoreCase(request.getVendorServiceDbId())) {
					service = Optional.of(p);
				}
			}
		}
		return service;
	}

	private boolean isBlank(VendorSpecificationServiceEntity service, PlatePositionEnum position) {
		return false;
	}

}
