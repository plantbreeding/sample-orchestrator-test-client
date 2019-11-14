package org.brapi.test.SampleOrchestratorServer.repository;

import org.brapi.test.SampleOrchestratorServer.model.entity.VendorSpecificationServiceEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorSpecificationServiceRepositoy extends PagingAndSortingRepository<VendorSpecificationServiceEntity, String>{

}
