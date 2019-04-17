package org.brapi.test.SampleOrchestratorServer.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SamplesResponseResult {

	@JsonProperty("data")
	private List<Sample> data = null;

	public List<Sample> getData() {
		return data;
	}

	public void setData(List<Sample> data) {
		this.data = data;
	}

}

