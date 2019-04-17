package org.brapi.test.SampleOrchestratorServer.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Plate {

	@JsonProperty("samples")
	private List<List<String>> samples;

	public List<List<String>> getSamples() {
		return samples;
	}

	public void setSamples(List<List<String>> samples) {
		this.samples = samples;
	}

}
