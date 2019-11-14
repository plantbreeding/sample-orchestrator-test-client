package org.brapi.test.SampleOrchestratorServer.service.genotyping;

public interface GenotypeDBServiceInterface {

	public void preUploadSetup();
	
	public void sendSampleData();
	
	public void sendDataMatrix();

	public String getAuthToken();
}
