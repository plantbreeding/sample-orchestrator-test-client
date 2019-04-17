package org.brapi.test.SampleOrchestratorServer.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="sample")
public class SampleEntity extends BaseEntity{
	@Column
	private String takenBy;
	@Column
	private Date sampleTimestamp;
	@Column
	private String sampleType;
	@Column
	private String tissueType;
	@Column
	private String notes;
	@Column
	private String concentration;
	@Column
	private String volume;
	@Column
	private String sampleExternalId;
	@ManyToOne
	private SampleGroupEntity group;
	
	public String getConcentration() {
		return concentration;
	}
	public void setConcentration(String concentration) {
		this.concentration = concentration;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public SampleGroupEntity getGroup() {
		return group;
	}
	public void setGroup(SampleGroupEntity group) {
		this.group = group;
	}
	public String getSampleExternalId() {
		return sampleExternalId;
	}
	public void setSampleExternalId(String sampleExternalId) {
		this.sampleExternalId = sampleExternalId;
	}
	public Date getSampleTimestamp() {
		return sampleTimestamp;
	}
	public void setSampleTimestamp(Date sampleTimestamp) {
		this.sampleTimestamp = sampleTimestamp;
	}
	public String getTakenBy() {
		return takenBy;
	}
	public void setTakenBy(String takenBy) {
		this.takenBy = takenBy;
	}
	public String getSampleType() {
		return sampleType;
	}
	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}
	public String getTissueType() {
		return tissueType;
	}
	public void setTissueType(String tissueType) {
		this.tissueType = tissueType;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}

}
