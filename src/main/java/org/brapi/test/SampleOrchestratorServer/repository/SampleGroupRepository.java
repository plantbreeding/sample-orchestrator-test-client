package org.brapi.test.SampleOrchestratorServer.repository;

import org.brapi.test.SampleOrchestratorServer.model.entity.SampleGroupEntity;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface SampleGroupRepository extends PagingAndSortingRepository<SampleGroupEntity, String>{

}
