package org.brapi.test.SampleOrchestratorServer.service.genotyping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenotypeDBServiceFactory {
	
	private DefaultGenotypeDBService defaultGenotypeDBService;
	private GobiiGenotypeDBService gobiiGenotypeDBService;
	
	@Autowired
	public GenotypeDBServiceFactory(DefaultGenotypeDBService defaultGenotypeDBService, GobiiGenotypeDBService gobiiGenotypeDBService) {
		this.defaultGenotypeDBService = defaultGenotypeDBService;
		this.gobiiGenotypeDBService = gobiiGenotypeDBService;
	}
	
	public GenotypeDBServiceInterface getGenotypeDbService(GenotypeDbServiceClassEnum serviceClass) {
		GenotypeDBServiceInterface service;
		switch (serviceClass) {
		case GOBII:
			service = gobiiGenotypeDBService;
			break;
		case DEFAULT:
		default:
			service = defaultGenotypeDBService;
			break;
		}
		return service;
	}
	
	public enum GenotypeDbServiceClassEnum{
		DEFAULT, GOBII;
	}
}
