package org.brapi.test.SampleOrchestratorServer.repository;

import org.brapi.test.SampleOrchestratorServer.model.entity.PlateEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PlateRepository extends PagingAndSortingRepository<PlateEntity, String> {

}
