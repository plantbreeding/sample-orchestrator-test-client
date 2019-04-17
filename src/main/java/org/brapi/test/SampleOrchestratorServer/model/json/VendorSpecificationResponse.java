package org.brapi.test.SampleOrchestratorServer.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VendorSpecificationResponse {

	@JsonProperty("metadata")
	private Metadata metadata = null;

	@JsonProperty("result")
	private VendorSpecification result = null;

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public VendorSpecification getResult() {
		return result;
	}

	public void setResult(VendorSpecification result) {
		this.result = result;
	}

}
