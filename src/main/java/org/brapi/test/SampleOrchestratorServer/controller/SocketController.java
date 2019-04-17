package org.brapi.test.SampleOrchestratorServer.controller;

import java.util.Optional;

import org.brapi.test.SampleOrchestratorServer.model.entity.SubmissionEntity;
import org.brapi.test.SampleOrchestratorServer.model.entity.SubmissionStageEnum;
import org.brapi.test.SampleOrchestratorServer.model.json.SubmissionStatusResponse;
import org.brapi.test.SampleOrchestratorServer.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class SocketController {

	private SimpMessagingTemplate simpMessagingTemplate;
	private SubmissionRepository submissionRepository;

	@Autowired
	public SocketController(SubmissionRepository submissionRepository, SimpMessagingTemplate simpMessagingTemplate) {
		this.submissionRepository = submissionRepository;
		this.simpMessagingTemplate = simpMessagingTemplate;
	}

	@MessageMapping("/status")
	@SendTo("/topic/status")
	public SubmissionStatusResponse greeting(SubmissionStatusResponse message) throws Exception {
		Thread.sleep(1000); // simulated delay
		return message;
	}

	public void pushSubmissionStatus(String submissionId) {
		SubmissionStatusResponse response = new SubmissionStatusResponse();
		Optional<SubmissionEntity> submissionOpt = submissionRepository.findById(submissionId);
		
		if (submissionOpt.isPresent()) {
			response.setId(submissionOpt.get().getId());
			response.setStage(submissionOpt.get().getSubmissionStage().name());
			response.setStatus(findStatus(submissionOpt.get()));
			response.setErrorMsg(submissionOpt.get().getErrorMsg());
			response.setVendorName(submissionOpt.get().getVendor().getVendorName());
		}		
		simpMessagingTemplate.convertAndSend("/topic/status", response);
	}
	
	private String findStatus(SubmissionEntity submission) {
		String status = "(...)";
		if (submission.getSubmissionStage() == SubmissionStageEnum.WAITING_FOR_VENDOR
				&& submission.getVendor_LastStatus() != null) {
			status = submission.getVendor_LastStatus().name();
		}
		if (submission.getSubmissionStage() == SubmissionStageEnum.WAITING_FOR_GENOTYPE_DATABASE
				&& submission.getGenotypeDB_LastStatus() != null) {
			status = submission.getGenotypeDB_LastStatus();
		}
		return status;
	}
}
