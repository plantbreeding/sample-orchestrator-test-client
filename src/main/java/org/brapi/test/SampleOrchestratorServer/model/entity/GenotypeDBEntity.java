package org.brapi.test.SampleOrchestratorServer.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.brapi.test.SampleOrchestratorServer.service.genotyping.GenotypeDBServiceFactory.GenotypeDbServiceClassEnum;

@Entity
@Table(name = "genotype_db")
public class GenotypeDBEntity extends BaseEntity {
	@Column
	private String genotypeDbName;
	@Column
	private String genotypeDbBaseURL;
	@Column
	private GenotypeDbServiceClassEnum genotypeDbServiceClass;
	
	public String getGenotypeDbName() {
		return genotypeDbName;
	}
	public void setGenotypeDbName(String genotypeDbName) {
		this.genotypeDbName = genotypeDbName;
	}
	public String getGenotypeDbBaseURL() {
		return genotypeDbBaseURL;
	}
	public void setGenotypeDbBaseURL(String genotypeDbBaseURL) {
		this.genotypeDbBaseURL = genotypeDbBaseURL;
	}
	public GenotypeDbServiceClassEnum getGenotypeDbServiceClass() {
		return genotypeDbServiceClass;
	}
	public void setGenotypeDbServiceClass(GenotypeDbServiceClassEnum genotypeDbServiceClass) {
		this.genotypeDbServiceClass = genotypeDbServiceClass;
	}
	
}