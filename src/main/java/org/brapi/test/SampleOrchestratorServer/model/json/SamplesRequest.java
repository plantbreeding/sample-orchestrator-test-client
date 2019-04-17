package org.brapi.test.SampleOrchestratorServer.model.json;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SamplesRequest {
	@JsonProperty("samples")
	List<Sample> samples;

	@JsonProperty("sampleListId")
	String sampleListId;

	public List<Sample> getSamples() {
		return samples;
	}

	public void setSamples(List<Sample> samples) {
		this.samples = samples;
	}

	public String getSampleListId() {
		return sampleListId;
	}

	public void setSampleListId(String sampleListId) {
		this.sampleListId = sampleListId;
	}
}
