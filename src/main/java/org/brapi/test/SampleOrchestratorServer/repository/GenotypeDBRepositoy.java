package org.brapi.test.SampleOrchestratorServer.repository;

import org.brapi.test.SampleOrchestratorServer.model.entity.GenotypeDBEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenotypeDBRepositoy extends PagingAndSortingRepository<GenotypeDBEntity, String>{

}
