package org.brapi.test.SampleOrchestratorServer.service.vendor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DartVendorService implements VendorServiceInterface {

	@Value("${vendor.dart.token}")
	private String fiveYearToken;
	
	@Value("${vendor.cornell.projectId}")
	private String projectId;
	
	@Override
	public String getAuthToken() {
		return "Bearer " + fiveYearToken;
	}

	@Override
	public Map<String, String> getRequiredServiceInfo() {
		Map<String, String> serviceInfo = new HashMap<>();
		serviceInfo.put("projectId", projectId);
		return serviceInfo;
	}

	@Override
	public boolean isFullOrder() {
		return false;
	}

	@Override
	public String getClientId() {
		return "peter";
	}

}
