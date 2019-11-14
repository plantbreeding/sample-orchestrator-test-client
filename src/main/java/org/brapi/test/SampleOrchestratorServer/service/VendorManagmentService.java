package org.brapi.test.SampleOrchestratorServer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.brapi.test.SampleOrchestratorServer.model.entity.VendorContactEntity;
import org.brapi.test.SampleOrchestratorServer.model.entity.VendorEntity;
import org.brapi.test.SampleOrchestratorServer.model.entity.VendorSpecificationEntity;
import org.brapi.test.SampleOrchestratorServer.model.entity.VendorSpecificationServiceEntity;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorContact;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorSpecification;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorSpecificationService;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorSummary;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorSummaryService;
import org.brapi.test.SampleOrchestratorServer.repository.VendorRepositoy;
import org.brapi.test.SampleOrchestratorServer.repository.VendorSpecificationServiceRepositoy;
import org.brapi.test.SampleOrchestratorServer.service.vendor.VendorServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class VendorManagmentService {
	private VendorServiceFactory vendorServiceFactory;
	private RestClientService restClientService;

	private VendorRepositoy vendorRepositoy;
	private VendorSpecificationServiceRepositoy vendorSpecificationServiceRepositoy;

	@Autowired
	public VendorManagmentService(RestClientService restClientService, VendorRepositoy vendorRepositoy,
			VendorSpecificationServiceRepositoy vendorSpecificationServiceRepositoy,
			VendorServiceFactory vendorServiceFactory) {

		this.vendorServiceFactory = vendorServiceFactory;
		this.restClientService = restClientService;

		this.vendorRepositoy = vendorRepositoy;
		this.vendorSpecificationServiceRepositoy = vendorSpecificationServiceRepositoy;
	}

	public List<VendorSummary> getAllVendors() {
		updateVendorSpecs();
		Page<VendorEntity> entities = vendorRepositoy.findAll(PageRequest.of(0, 100, Sort.unsorted()));
		List<VendorSummary> summaries = entities.stream().filter((entity) -> {
			return entity.getVendorSpec() != null;
		}).map(this::convertFromEntity).collect(Collectors.toList());
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
			} else {
				entity.setVendorSpec(null);
				vendorRepositoy.save(entity);
			}
		}
	}

	private VendorSpecificationEntity updateEntity(VendorSpecificationEntity entity, VendorSpecification spec) {
		if (spec.getServices() != null) {
			List<VendorSpecificationService> services = spec.getServices();
			List<VendorSpecificationServiceEntity> serviceEntities = new ArrayList<>();
			for (VendorSpecificationService service : services) {
				VendorSpecificationServiceEntity serviceEntity = null;
				for (VendorSpecificationServiceEntity oldServiceEntity : entity.getServices()) {
					if (service.getServiceId() == oldServiceEntity.getServiceId()) {
						serviceEntity = oldServiceEntity;
						break;
					}
				}
				if (serviceEntity == null) {
					serviceEntity = new VendorSpecificationServiceEntity();
					serviceEntity.setServiceId(service.getServiceId());
				}
				serviceEntity.setServiceDescription(service.getServiceDescription());
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
