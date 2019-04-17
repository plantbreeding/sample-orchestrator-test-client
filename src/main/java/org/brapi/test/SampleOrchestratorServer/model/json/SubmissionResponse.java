package org.brapi.test.SampleOrchestratorServer.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubmissionResponse {

	@JsonProperty("metadata")
	private Metadata metadata = null;

	@JsonProperty("result")
	private SubmissionResponseResult result = null;

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public SubmissionResponseResult getResult() {
		return result;
	}

	public void setResult(SubmissionResponseResult result) {
		this.result = result;
	}

}
