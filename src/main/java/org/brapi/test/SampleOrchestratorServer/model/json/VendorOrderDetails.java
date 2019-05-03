package org.brapi.test.SampleOrchestratorServer.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class VendorOrderDetails {
	@JsonProperty("orderId")
	private String orderId = null;

	@JsonProperty("clientId")
	private String clientId = null;

	@JsonProperty("serviceId")
	private String serviceId = null;

	@JsonProperty("numberOfSamples")
	private Integer numberOfSamples = null;

	@JsonProperty("requiredServiceInfo")
	private Map<String, String> requiredServiceInfo = null;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getNumberOfSamples() {
		return numberOfSamples;
	}

	public void setNumberOfSamples(Integer numberOfSamples) {
		this.numberOfSamples = numberOfSamples;
	}

	public Map<String, String> getRequiredServiceInfo() {
		return requiredServiceInfo;
	}

	public void setRequiredServiceInfo(Map<String, String> requiredServiceInfo) {
		this.requiredServiceInfo = requiredServiceInfo;
	}

}
