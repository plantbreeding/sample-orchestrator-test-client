package org.brapi.test.SampleOrchestratorServer.service.genotyping;

import org.springframework.stereotype.Service;

@Service
public class DefaultGenotypeDBService implements GenotypeDBServiceInterface {

	@Override
	public void preUploadSetup() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendSampleData() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendDataMatrix() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getAuthToken() {
		return "Bearer YYYY";
	}

}
