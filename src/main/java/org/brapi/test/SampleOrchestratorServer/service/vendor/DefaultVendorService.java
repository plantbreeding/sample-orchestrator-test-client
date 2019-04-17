package org.brapi.test.SampleOrchestratorServer.service.vendor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class DefaultVendorService implements VendorServiceInterface {

	@Override
	public String getAuthToken() {
		return "Bearer YYYY";
	}

	@Override
	public Map<String, String> getRequiredServiceInfo() {
		return new HashMap<>();
	}

}
