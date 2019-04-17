package org.brapi.test.SampleOrchestratorServer.service.vendor;

import java.util.Map;

public interface VendorServiceInterface {

	public String getAuthToken();
	
	public Map<String, String> getRequiredServiceInfo();
}
