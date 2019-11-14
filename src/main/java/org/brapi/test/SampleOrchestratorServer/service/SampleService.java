package org.brapi.test.SampleOrchestratorServer.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.brapi.test.SampleOrchestratorServer.model.entity.SampleGroupEntity;
import org.brapi.test.SampleOrchestratorServer.model.entity.SampleEntity;
import org.brapi.test.SampleOrchestratorServer.model.json.Sample;
import org.brapi.test.SampleOrchestratorServer.model.json.SamplesRequest;
import org.brapi.test.SampleOrchestratorServer.repository.SampleGroupRepository;
import org.brapi.test.SampleOrchestratorServer.repository.SampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SampleService {
	private SampleRepository sampleRepository;
	private SampleGroupRepository sampleGroupRepository;

	@Autowired
	public SampleService(SampleRepository sampleRepository, SampleGroupRepository listRepository) {
		this.sampleRepository = sampleRepository;
		this.sampleGroupRepository = listRepository;
	}

	public SampleEntity saveSample(Sample sample) {		
		SampleEntity entity = new SampleEntity();
		entity.setNotes(sample.getNotes());
		entity.setSampleExternalId(sample.getSampleExternalId());
		entity.setSampleTimestamp(DateUtility.toDate(sample.getSampleTimestamp()));
		entity.setSampleType(sample.getSampleType());
		entity.setTakenBy(sample.getTakenBy());
		entity.setTissueType(sample.getTissueType());

		entity = sampleRepository.save(entity);
		
		return entity;
	}

	public Sample getSample(String sampleId) {

		Optional<SampleEntity> detailOptional = sampleRepository.findById(sampleId);
		Sample sample = null;

		if (detailOptional.isPresent()) {
			sample = convertFromEntity(detailOptional.get());
		}
		return sample;
	}

	private Sample convertFromEntity(SampleEntity entity) {
		Sample sample = new Sample();

		sample.setNotes(entity.getNotes());
		sample.setSampleExternalId(entity.getSampleExternalId());
		sample.setSampleDbId(entity.getId());
		sample.setSampleTimestamp(DateUtility.toDateString(entity.getSampleTimestamp()));
		sample.setSampleType(entity.getSampleType());
		sample.setTakenBy(entity.getTakenBy());
		sample.setTissueType(entity.getTissueType());

		return sample;
	}

	public List<Sample> saveSamples(@Valid SamplesRequest request) {
		List<Sample> savedSamples = new ArrayList<>();
		SampleGroupEntity sampleGroupEntity = new SampleGroupEntity();
		sampleGroupEntity.setSamples(new HashSet<>());
		sampleGroupEntity.setName(request.getSampleListId());
		
		for(Sample newSample: request.getSamples()) {
			SampleEntity savedSample = saveSample(newSample);
			sampleGroupEntity.getSamples().add(savedSample);
			savedSamples.add(convertFromEntity(savedSample));
		}
		
		sampleGroupRepository.save(sampleGroupEntity);
		return savedSamples;
	}
}
