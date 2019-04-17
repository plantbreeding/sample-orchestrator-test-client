package org.brapi.test.SampleOrchestratorServer.repository;

import org.brapi.test.SampleOrchestratorServer.model.entity.SubmissionEntity;
import org.brapi.test.SampleOrchestratorServer.model.entity.SubmissionStageEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SubmissionRepository extends PagingAndSortingRepository<SubmissionEntity, String> {
	public Page<SubmissionEntity> findBySubmissionStage(SubmissionStageEnum submissionStage, Pageable pageReq);
}
