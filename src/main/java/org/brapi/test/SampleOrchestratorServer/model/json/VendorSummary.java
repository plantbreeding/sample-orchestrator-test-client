package org.brapi.test.SampleOrchestratorServer.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VendorSummary {

	@JsonProperty("vendorDbId")
	private String vendorDbId = null;

	@JsonProperty("vendorName")
	private String vendorName = null;
	
	@JsonProperty("vendorServices")
	private List<VendorSummaryService> vendorServices = null;

	public List<VendorSummaryService> getVendorServices() {
		return vendorServices;
	}

	public void setVendorServices(List<VendorSummaryService> vendorServices) {
		this.vendorServices = vendorServices;
	}

	public String getVendorDbId() {
		return vendorDbId;
	}

	public void setVendorDbId(String vendorDbId) {
		this.vendorDbId = vendorDbId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	
}
