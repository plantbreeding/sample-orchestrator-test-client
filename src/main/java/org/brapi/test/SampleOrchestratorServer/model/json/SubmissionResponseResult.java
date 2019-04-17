package org.brapi.test.SampleOrchestratorServer.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubmissionResponseResult {

	@JsonProperty("data")
	private List<Plate> data = null;
	
	@JsonProperty("submissionDbId")
	private String submissionDbId;

	public String getSubmissionDbId() {
		return submissionDbId;
	}

	public void setSubmissionDbId(String submissionDbId) {
		this.submissionDbId = submissionDbId;
	}

	public List<Plate> getData() {
		return data;
	}

	public void setData(List<Plate> data) {
		this.data = data;
	}
	
}
