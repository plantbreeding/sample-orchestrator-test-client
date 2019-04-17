package org.brapi.test.SampleOrchestratorServer.model.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "sample_group")
public class SampleGroupEntity extends BaseEntity {
	@Column
	private Date dateCreated;
	@Column
	private Date dateModified;
	@Column
	private String description;
	@Column
	private String name;
	@OneToMany(mappedBy="group", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<SampleEntity> samples;
	@OneToMany(mappedBy="group", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<PlateEntity> plates;

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<SampleEntity> getSamples() {
		return samples;
	}

	public void setSamples(Set<SampleEntity> samples) {
		this.samples = samples;
	}

	public Set<PlateEntity> getPlates() {
		return plates;
	}

	public void setPlates(Set<PlateEntity> plates) {
		this.plates = plates;
	}

	public void addPlate(PlateEntity plate) {
		if(getPlates() == null)
			setPlates(new HashSet<>());
		
		getPlates().add(plate);
	}
	
	
}