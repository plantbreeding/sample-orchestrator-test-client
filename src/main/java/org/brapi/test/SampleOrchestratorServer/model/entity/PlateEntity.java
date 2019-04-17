package org.brapi.test.SampleOrchestratorServer.model.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name="plate")
public class PlateEntity extends BaseEntity{
	@Column
	private Boolean vertical;
	@OneToMany(mappedBy="plate", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
    private Set<PlatePositionEntity> samples;
	@ManyToOne
	private SampleGroupEntity group;

	public Boolean isVertical() {
		return vertical;
	}
	public Boolean isHorizontal() {
		return !vertical;
	}
	public void setVertical() {
		this.vertical = true;
	}
	public void setHorizontal() {
		this.vertical = false;
	}
	public Set<PlatePositionEntity> getSamples() {
		return samples;
	}
	public void setSamples(Set<PlatePositionEntity> samples) {
		this.samples = samples;
	}
	public SampleGroupEntity getGroup() {
		return group;
	}
	public void setGroup(SampleGroupEntity group) {
		this.group = group;
	}
	public void addSample(PlatePositionEntity platePostion) {
		if(getSamples() == null)
			setSamples(new HashSet<>());
		
		getSamples().add(platePostion);
	}
}
