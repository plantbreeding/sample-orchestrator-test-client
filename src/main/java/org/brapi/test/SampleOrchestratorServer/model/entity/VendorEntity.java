package org.brapi.test.SampleOrchestratorServer.model.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.brapi.test.SampleOrchestratorServer.service.vendor.VendorServiceFactory.VendorServiceClassEnum;

@Entity
@Table(name = "vendor")
public class VendorEntity extends BaseEntity {
	@Column
	private String vendorName;
	@Column
	private String vendorBaseURL;
	@Column
	private VendorServiceClassEnum vendorServiceClass;
	@OneToOne(cascade=CascadeType.ALL)
	private VendorSpecificationEntity vendorSpec;
	
	public VendorServiceClassEnum getVendorServiceClass() {
		return vendorServiceClass;
	}
	public void setVendorServiceClass(VendorServiceClassEnum vendorServiceClass) {
		this.vendorServiceClass = vendorServiceClass;
	}
	public VendorSpecificationEntity getVendorSpec() {
		return vendorSpec;
	}
	public void setVendorSpec(VendorSpecificationEntity vendorSpec) {
		this.vendorSpec = vendorSpec;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getVendorBaseURL() {
		return vendorBaseURL;
	}
	public void setVendorBaseURL(String vendorBaseURL) {
		this.vendorBaseURL = vendorBaseURL;
	}
	
	
}