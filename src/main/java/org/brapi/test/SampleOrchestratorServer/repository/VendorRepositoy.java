package org.brapi.test.SampleOrchestratorServer.repository;

import org.brapi.test.SampleOrchestratorServer.model.entity.VendorEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepositoy extends PagingAndSortingRepository<VendorEntity, String>{

}
