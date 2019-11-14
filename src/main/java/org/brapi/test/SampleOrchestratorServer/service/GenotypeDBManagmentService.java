package org.brapi.test.SampleOrchestratorServer.service;

import java.util.List;
import org.brapi.test.SampleOrchestratorServer.model.entity.GenotypeDBEntity;
import org.brapi.test.SampleOrchestratorServer.model.json.GenotypeDBSummary;
import org.brapi.test.SampleOrchestratorServer.repository.GenotypeDBRepositoy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class GenotypeDBManagmentService {	
	private GenotypeDBRepositoy genotypeDBRepositoy;

	@Autowired
	public GenotypeDBManagmentService(GenotypeDBRepositoy genotypeDBRepositoy) {
		this.genotypeDBRepositoy = genotypeDBRepositoy;
	}

	public List<GenotypeDBSummary> getAllVendors() {
		Page<GenotypeDBEntity> entities = genotypeDBRepositoy.findAll(PageRequest.of(0, 100, Sort.unsorted()));
		List<GenotypeDBSummary> summaries = entities.map(this::convertFromEntity).getContent();
		return summaries;
	}

	private GenotypeDBSummary convertFromEntity(GenotypeDBEntity entity) {
		GenotypeDBSummary genotypeDb = new GenotypeDBSummary();
		genotypeDb.setGenotypeDbId(entity.getId());
		genotypeDb.setGenotypeDbName(entity.getGenotypeDbName());
		return genotypeDb;
	}
}
