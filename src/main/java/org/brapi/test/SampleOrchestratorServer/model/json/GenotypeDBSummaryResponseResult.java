package org.brapi.test.SampleOrchestratorServer.model.json;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GenotypeDBSummaryResponseResult {

	@JsonProperty("data")
	private List<GenotypeDBSummary> data = null;

	public List<GenotypeDBSummary> getData() {
		return data;
	}

	public void setData(List<GenotypeDBSummary> data) {
		this.data = data;
	}

	public void addDataItem(GenotypeDBSummary vendorSummary) {
		if(this.data == null) {
			this.data = new ArrayList<>();
		}
		this.data.add(vendorSummary);
	}
	
	
}
