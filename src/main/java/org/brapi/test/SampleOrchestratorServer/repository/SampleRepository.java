package org.brapi.test.SampleOrchestratorServer.repository;

import org.brapi.test.SampleOrchestratorServer.model.entity.SampleEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleRepository extends PagingAndSortingRepository<SampleEntity, String> {

}
