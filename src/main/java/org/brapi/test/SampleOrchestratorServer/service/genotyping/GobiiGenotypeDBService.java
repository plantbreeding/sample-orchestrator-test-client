package org.brapi.test.SampleOrchestratorServer.service.genotyping;

import org.springframework.stereotype.Service;

@Service
public class GobiiGenotypeDBService implements GenotypeDBServiceInterface {

	@Override
	public void preUploadSetup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendSampleData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendDataMatrix() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAuthToken() {
		return "Bearer YYYY";
	}
}
