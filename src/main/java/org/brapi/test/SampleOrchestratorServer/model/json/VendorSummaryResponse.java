package org.brapi.test.SampleOrchestratorServer.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VendorSummaryResponse {

	@JsonProperty("metadata")
	private Metadata metadata = null;

	@JsonProperty("result")
	private VendorSummaryResponseResult result = null;

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public VendorSummaryResponseResult getResult() {
		return result;
	}

	public void setResult(VendorSummaryResponseResult result) {
		this.result = result;
	}

}
