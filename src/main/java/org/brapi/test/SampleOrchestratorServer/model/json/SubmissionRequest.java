package org.brapi.test.SampleOrchestratorServer.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubmissionRequest {

	@JsonProperty("sampleGroupId")
	private String sampleGroupId;

	@JsonProperty("vendorDbId")
	private String vendorDbId;

	@JsonProperty("vendorServiceDbId")
	private String vendorServiceDbId;
	
	@JsonProperty("genotypeDbId")
	private String genotypeDbId;
	
	public String getVendorServiceDbId() {
		return vendorServiceDbId;
	}

	public void setVendorServiceDbId(String vendorServiceDbId) {
		this.vendorServiceDbId = vendorServiceDbId;
	}

	public String getSampleGroupId() {
		return sampleGroupId;
	}

	public void setSampleGroupId(String sampleGroupId) {
		this.sampleGroupId = sampleGroupId;
	}

	public String getVendorDbId() {
		return vendorDbId;
	}

	public void setVendorDbId(String vendorDbId) {
		this.vendorDbId = vendorDbId;
	}

	public String getGenotypeDbId() {
		return genotypeDbId;
	}

	public void setGenotypeDbId(String genotypeDbId) {
		this.genotypeDbId = genotypeDbId;
	}

}
