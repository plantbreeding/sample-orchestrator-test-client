package org.brapi.test.SampleOrchestratorServer.service.vendor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CornellVendorService implements VendorServiceInterface {

	@Value("${vendor.cornell.token}")
	private String fiveYearToken;
	
	@Value("${vendor.cornell.projectId}")
	private String projectId;
	
	@Override
	public String getAuthToken() {
		return "Bearer:" + fiveYearToken;
	}

	@Override
	public Map<String, String> getRequiredServiceInfo() {
		Map<String, String> serviceInfo = new HashMap<>();
		serviceInfo.put("projectId", projectId);
		return serviceInfo;
	}

}
