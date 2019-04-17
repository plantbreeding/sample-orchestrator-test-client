package org.brapi.test.SampleOrchestratorServer.service.vendor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VendorServiceFactory {
	
	private DefaultVendorService defaultVendorService;
	private CornellVendorService cornellVendorService;
	
	@Autowired
	public VendorServiceFactory(DefaultVendorService defaultVendorService, CornellVendorService cornellVendorService) {
		this.defaultVendorService = defaultVendorService;
		this.cornellVendorService = cornellVendorService;
	}
	
	public VendorServiceInterface getVendorService(VendorServiceClassEnum serviceClass) {
		VendorServiceInterface service;
		switch (serviceClass) {
		case CORNELL:
			service = cornellVendorService;
			break;
		case DEFAULT:
		default:
			service = defaultVendorService;
			break;
		}
		return service;
	}
	
	public enum VendorServiceClassEnum{
		DEFAULT, CORNELL;
	}
}
