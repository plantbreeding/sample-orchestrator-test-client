package org.brapi.test.SampleOrchestratorServer.model.json;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VendorSummaryResponseResult {

	@JsonProperty("data")
	private List<VendorSummary> data = null;

	public List<VendorSummary> getData() {
		return data;
	}

	public void setData(List<VendorSummary> data) {
		this.data = data;
	}

	public void addDataItem(VendorSummary vendorSummary) {
		if(this.data == null) {
			this.data = new ArrayList<>();
		}
		this.data.add(vendorSummary);
	}
	
	
}
