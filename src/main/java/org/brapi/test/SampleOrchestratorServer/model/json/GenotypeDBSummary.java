package org.brapi.test.SampleOrchestratorServer.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GenotypeDBSummary {

	@JsonProperty("genotypeDbId")
	private String genotypeDbId = null;

	@JsonProperty("genotypeDbName")
	private String genotypeDbName = null;

	public String getGenotypeDbId() {
		return genotypeDbId;
	}

	public void setGenotypeDbId(String genotypeDbId) {
		this.genotypeDbId = genotypeDbId;
	}

	public String getGenotypeDbName() {
		return genotypeDbName;
	}

	public void setGenotypeDbName(String genotypeDbName) {
		this.genotypeDbName = genotypeDbName;
	}
	
}
