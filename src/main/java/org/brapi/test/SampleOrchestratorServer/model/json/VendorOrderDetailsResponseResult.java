package org.brapi.test.SampleOrchestratorServer.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class VendorOrderDetailsResponseResult {
	@JsonProperty("data")
	private List<VendorOrderDetails> data = null;

	public List<VendorOrderDetails> getData() {
		return data;
	}

	public void setData(List<VendorOrderDetails> data) {
		this.data = data;
	}

}
