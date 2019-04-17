package org.brapi.test.SampleOrchestratorServer.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VendorSummaryService {

	@JsonProperty("serviceDbId")
	private String serviceDbId = null;

	@JsonProperty("serviceName")
	private String serviceName = null;

	public String getServiceDbId() {
		return serviceDbId;
	}

	public void setServiceDbId(String serviceDbId) {
		this.serviceDbId = serviceDbId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
}
