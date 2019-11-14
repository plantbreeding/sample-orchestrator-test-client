package org.brapi.test.SampleOrchestratorServer.model.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name="plate_position")
public class PlatePositionEntity extends BaseEntity{
	@Column
    private PlatePositionEnum position;
	@OneToOne(cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
    private SampleEntity sample;
	@ManyToOne
	private PlateEntity plate;
	
	public PlatePositionEnum getPosition() {
		return position;
	}
	public void setPosition(PlatePositionEnum position) {
		this.position = position;
	}
	public SampleEntity getSample() {
		return sample;
	}
	public void setSample(SampleEntity sample) {
		this.sample = sample;
	}
	public PlateEntity getPlate() {
		return plate;
	}
	public void setPlate(PlateEntity plate) {
		this.plate = plate;
	}
	
}
