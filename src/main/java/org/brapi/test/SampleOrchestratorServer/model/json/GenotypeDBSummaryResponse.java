package org.brapi.test.SampleOrchestratorServer.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GenotypeDBSummaryResponse {

	@JsonProperty("metadata")
	private Metadata metadata = null;

	@JsonProperty("result")
	private GenotypeDBSummaryResponseResult result = null;

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public GenotypeDBSummaryResponseResult getResult() {
		return result;
	}

	public void setResult(GenotypeDBSummaryResponseResult result) {
		this.result = result;
	}

}
