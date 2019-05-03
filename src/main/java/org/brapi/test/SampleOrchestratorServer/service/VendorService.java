package org.brapi.test.SampleOrchestratorServer.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.brapi.test.SampleOrchestratorServer.controller.SocketController;
import org.brapi.test.SampleOrchestratorServer.exceptions.BrAPIServerException;
import org.brapi.test.SampleOrchestratorServer.model.entity.PlateEntity;
import org.brapi.test.SampleOrchestratorServer.model.entity.PlatePositionEntity;
import org.brapi.test.SampleOrchestratorServer.model.entity.PlatePositionEnum;
import org.brapi.test.SampleOrchestratorServer.model.entity.SampleEntity;
import org.brapi.test.SampleOrchestratorServer.model.entity.SampleGroupEntity;
import org.brapi.test.SampleOrchestratorServer.model.entity.SubmissionEntity;
import org.brapi.test.SampleOrchestratorServer.model.entity.SubmissionStageEnum;
import org.brapi.test.SampleOrchestratorServer.model.entity.VendorContactEntity;
import org.brapi.test.SampleOrchestratorServer.model.entity.VendorEntity;
import org.brapi.test.SampleOrchestratorServer.model.entity.VendorSpecificationEntity;
import org.brapi.test.SampleOrchestratorServer.model.entity.VendorSpecificationServiceEntity;
import org.brapi.test.SampleOrchestratorServer.model.json.SubmissionRequest;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorContact;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorSpecification;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorSpecificationService;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorSummary;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorSummaryService;
import org.brapi.test.SampleOrchestratorServer.repository.SampleGroupRepository;
import org.brapi.test.SampleOrchestratorServer.repository.SubmissionRepository;
import org.brapi.test.SampleOrchestratorServer.repository.VendorRepositoy;
import org.brapi.test.SampleOrchestratorServer.repository.VendorSpecificationServiceRepositoy;
import org.brapi.test.SampleOrchestratorServer.service.vendor.VendorServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class VendorService {
	private SocketController socketController;
	private RestClientService restClientService;
	private VendorRepositoy vendorRepositoy;
	private VendorSpecificationServiceRepositoy vendorSpecificationServiceRepositoy;
	private VendorServiceFactory vendorServiceFactory;
	private SampleGroupRepository sampleGroupRepository;
	private SubmissionRepository submissionRepository;

	@Autowired
	public VendorService(RestClientService restClientService, VendorRepositoy vendorRepositoy,
			SampleGroupRepository sampleGroupRepository, SubmissionRepository submissionRepository,
			VendorSpecificationServiceRepositoy vendorSpecificationServiceRepositoy,
			VendorServiceFactory vendorServiceFactory, SocketController socketController) {
		this.restClientService = restClientService;
		this.vendorRepositoy = vendorRepositoy;
		this.sampleGroupRepository = sampleGroupRepository;
		this.submissionRepository = submissionRepository;
		this.vendorSpecificationServiceRepositoy = vendorSpecificationServiceRepositoy;
		this.vendorServiceFactory = vendorServiceFactory;
		this.socketController = socketController;
	}

	public SubmissionEntity buildSubmission(SubmissionRequest request) throws BrAPIServerException {
		SubmissionEntity submission = new SubmissionEntity();
		Optional<VendorEntity> vendorOpt = vendorRepositoy.findById(request.getVendorDbId());
		Optional<VendorSpecificationServiceEntity> serviceOpt = findService(request);
		Optional<SampleGroupEntity> sampleGroupOpt = sampleGroupRepository.findById(request.getSampleGroupId());

		if (serviceOpt.isPresent() && sampleGroupOpt.isPresent() && vendorOpt.isPresent()) {
			SampleGroupEntity sampleGroup = sampleGroupOpt.get();
			sampleGroup.setPlates(buildPlates(sampleGroup.getSamples(), serviceOpt.get(), sampleGroup));

			sampleGroup = sampleGroupRepository.save(sampleGroup);
			submission.setSampleGroup(sampleGroup);
			submission.setVendorService(serviceOpt.get());
			submission.setSubmissionStage(SubmissionStageEnum.NEW_SUBMISSION);
			submission.setVendor(vendorOpt.get());
			submission = submissionRepository.save(submission);
		}
		return submission;
	}

	private Set<PlateEntity> buildPlates(Set<SampleEntity> samples, VendorSpecificationServiceEntity service,
			SampleGroupEntity sampleGroup) {
		Set<PlateEntity> plateEntities = new HashSet<>();
		PlateEntity plate = new PlateEntity();

		Iterator<PlatePositionEnum> positionsIter = Arrays.asList(PlatePositionEnum.values()).iterator();
		Iterator<SampleEntity> sampleIter = samples.iterator();

		while (sampleIter.hasNext()) {
			PlatePositionEnum pos = findNextPosition(service, positionsIter);
			if (pos != null) {
				PlatePositionEntity platePostion = new PlatePositionEntity();
				platePostion.setPosition(pos);
				platePostion.setSample(sampleIter.next());
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

	public List<VendorSummary> getAllVendors() {
		updateVendorSpecs();
		Page<VendorEntity> entities = vendorRepositoy.findAll(PageRequest.of(0, 100, Sort.unsorted()));
		List<VendorSummary> summaries = entities.map(this::convertFromEntity).getContent();
		return summaries;
	}

	private void updateVendorSpecs() {
		Page<VendorEntity> entities = vendorRepositoy.findAll(PageRequest.of(0, 100, Sort.unsorted()));
		for (VendorEntity entity : entities) {
			String authToken = vendorServiceFactory.getVendorService(entity.getVendorServiceClass()).getAuthToken();
			VendorSpecification spec = restClientService.getVendorSpecifications(authToken, entity);
			if (spec != null) {
				VendorSpecificationEntity specEntity = updateEntity(entity.getVendorSpec(), spec);
				entity.setVendorSpec(specEntity);
				vendorRepositoy.save(entity);
			}
		}
	}

	private VendorSpecificationEntity updateEntity(VendorSpecificationEntity entity, VendorSpecification spec) {
		if (spec.getServices() != null) {
			for (VendorSpecificationServiceEntity oldServiceEntity : entity.getServices()) {
				vendorSpecificationServiceRepositoy.delete(oldServiceEntity);
			}
			List<VendorSpecificationService> services = spec.getServices();
			List<VendorSpecificationServiceEntity> serviceEntities = new ArrayList<>();
			for (VendorSpecificationService service : services) {
				VendorSpecificationServiceEntity serviceEntity = new VendorSpecificationServiceEntity();
				serviceEntity.setServiceDescription(service.getServiceDescription());
				serviceEntity.setServiceId(service.getServiceId());
				serviceEntity.setServiceName(service.getServiceName());
				serviceEntity.setServicePlatformMarkerType(service.getServicePlatformMarkerType());
				serviceEntity.setServicePlatformName(service.getServicePlatformName());
				serviceEntity.setSpecificRequirements(service.getSpecificRequirements());
				serviceEntity.setVendorSpecification(entity);
				vendorSpecificationServiceRepositoy.save(serviceEntity);
				serviceEntities.add(serviceEntity);
			}
			entity.setServices(serviceEntities);
		}
		if (spec.getVendorContact() != null) {
			VendorContact contact = spec.getVendorContact();
			if (entity.getVendorContact() == null) {
				entity.setVendorContact(new VendorContactEntity());
			}
			VendorContactEntity contactEntity = entity.getVendorContact();
			contactEntity.setVendorAddress(contact.getVendorAddress());
			contactEntity.setVendorCity(contact.getVendorCity());
			contactEntity.setVendorContactName(contact.getVendorContactName());
			contactEntity.setVendorCountry(contact.getVendorCountry());
			contactEntity.setVendorDescription(contact.getVendorDescription());
			contactEntity.setVendorEmail(contact.getVendorEmail());
			contactEntity.setVendorName(contact.getVendorName());
			contactEntity.setVendorPhone(contact.getVendorPhone());
			contactEntity.setVendorURL(contact.getVendorURL());
		}
		if (spec.getAdditionalInfo() != null) {
			entity.setAdditionalInfo(spec.getAdditionalInfo());
		}
		return entity;
	}

	private VendorSummary convertFromEntity(VendorEntity entity) {
		VendorSummary vendor = new VendorSummary();
		vendor.setVendorDbId(entity.getId());
		vendor.setVendorName(entity.getVendorName());
		vendor.setVendorServices(new ArrayList<>());
		for (VendorSpecificationServiceEntity p : entity.getVendorSpec().getServices()) {
			VendorSummaryService service = new VendorSummaryService();
			service.setServiceDbId(p.getId());
			service.setServiceName(p.getServiceName());
			vendor.getVendorServices().add(service);
		}
		return vendor;
	}
}
