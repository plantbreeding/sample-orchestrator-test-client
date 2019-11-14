package org.brapi.test.SampleOrchestratorServer.model.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.brapi.test.SampleOrchestratorServer.model.json.VendorOrderStatusResponseResult.StatusEnum;

@Entity
@Table(name = "submission")
public class SubmissionEntity extends BaseEntity {
	@Column
	private SubmissionStageEnum submissionStage;
	@Column
	private Integer vendor_PollingAttempts;
	@Column
	private StatusEnum vendor_LastStatus;
	@Column
	private String vendor_OrderId;
	@Column
	private String vendor_SamplesId;
	@Column
	private Integer genotypeDB_PollingAttempts;
	@Column
	private String genotypeDB_LastStatus;
	@Column
	private String genotypeDB_OrderId;
	@Column
	private String errorMsg;
	@OneToOne(cascade=CascadeType.ALL)
	private SampleGroupEntity sampleGroup;
	@ManyToOne
	@JoinTable(name = "submission_vendor", 
	joinColumns = @JoinColumn(name = "vendor_id", referencedColumnName = "id"), 
	inverseJoinColumns = @JoinColumn(name = "submission_id", referencedColumnName = "id"))
	private VendorEntity vendor;
	@ManyToOne
	@JoinTable(name = "submission_vendor_service", 
	joinColumns = @JoinColumn(name = "vendor_service_id", referencedColumnName = "id"), 
	inverseJoinColumns = @JoinColumn(name = "submission_id", referencedColumnName = "id"))
	private VendorSpecificationServiceEntity vendorService;
	@ManyToOne
	@JoinTable(name = "submission_genotype_db", 
	joinColumns = @JoinColumn(name = "genotype_db_id", referencedColumnName = "id"), 
	inverseJoinColumns = @JoinColumn(name = "submission_id", referencedColumnName = "id"))
	private GenotypeDBEntity genotypeDB;
	
	public String getVendor_SamplesId() {
		return vendor_SamplesId;
	}
	public void setVendor_SamplesId(String vendor_SamplesId) {
		this.vendor_SamplesId = vendor_SamplesId;
	}
	public VendorEntity getVendor() {
		return vendor;
	}
	public void setVendor(VendorEntity vendor) {
		this.vendor = vendor;
	}
	public String getShortId() {
		return this.getId().substring(0, 8);
	}
	public VendorSpecificationServiceEntity getVendorService() {
		return vendorService;
	}
	public void setVendorService(VendorSpecificationServiceEntity vendorService) {
		this.vendorService = vendorService;
	}
	public SampleGroupEntity getSampleGroup() {
		return sampleGroup;
	}
	public void setSampleGroup(SampleGroupEntity sampleGroup) {
		this.sampleGroup = sampleGroup;
	}
	public String getVendor_OrderId() {
		return vendor_OrderId;
	}
	public void setVendor_OrderId(String vendor_OrderId) {
		this.vendor_OrderId = vendor_OrderId;
	}
	public String getGenotypeDB_OrderId() {
		return genotypeDB_OrderId;
	}
	public void setGenotypeDB_OrderId(String genotypeDB_OrderId) {
		this.genotypeDB_OrderId = genotypeDB_OrderId;
	}
	public SubmissionStageEnum getSubmissionStage() {
		return submissionStage;
	}
	public void setSubmissionStage(SubmissionStageEnum submissionStage) {
		this.submissionStage = submissionStage;
	}
	public Integer getVendor_PollingAttempts() {
		return vendor_PollingAttempts;
	}
	public void setVendor_PollingAttempts(Integer vendor_PollingAttempts) {
		this.vendor_PollingAttempts = vendor_PollingAttempts;
	}
	public StatusEnum getVendor_LastStatus() {
		return vendor_LastStatus;
	}
	public void setVendor_LastStatus(StatusEnum statusEnum) {
		this.vendor_LastStatus = statusEnum;
	}
	public Integer getGenotypeDB_PollingAttempts() {
		return genotypeDB_PollingAttempts;
	}
	public void setGenotypeDB_PollingAttempts(Integer genotypeDB_PollingAttempts) {
		this.genotypeDB_PollingAttempts = genotypeDB_PollingAttempts;
	}
	public String getGenotypeDB_LastStatus() {
		return genotypeDB_LastStatus;
	}
	public void setGenotypeDB_LastStatus(String genotypeDB_LastStatus) {
		this.genotypeDB_LastStatus = genotypeDB_LastStatus;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public GenotypeDBEntity getGenotypeDB() {
		return genotypeDB;
	}
	public void setGenotypeDB(GenotypeDBEntity genotypeDB) {
		this.genotypeDB = genotypeDB;
	}
}