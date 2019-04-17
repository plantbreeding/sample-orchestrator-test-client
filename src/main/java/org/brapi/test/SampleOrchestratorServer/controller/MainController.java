package org.brapi.test.SampleOrchestratorServer.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.brapi.test.SampleOrchestratorServer.exceptions.BrAPIServerException;
import org.brapi.test.SampleOrchestratorServer.model.entity.SubmissionEntity;
import org.brapi.test.SampleOrchestratorServer.model.json.Metadata;
import org.brapi.test.SampleOrchestratorServer.model.json.MetadataPagination;
import org.brapi.test.SampleOrchestratorServer.model.json.Plate;
import org.brapi.test.SampleOrchestratorServer.model.json.Sample;
import org.brapi.test.SampleOrchestratorServer.model.json.SamplesRequest;
import org.brapi.test.SampleOrchestratorServer.model.json.SamplesResponse;
import org.brapi.test.SampleOrchestratorServer.model.json.SamplesResponseResult;
import org.brapi.test.SampleOrchestratorServer.model.json.Status;
import org.brapi.test.SampleOrchestratorServer.model.json.SubmissionRequest;
import org.brapi.test.SampleOrchestratorServer.model.json.SubmissionResponse;
import org.brapi.test.SampleOrchestratorServer.model.json.SubmissionResponseResult;
import org.brapi.test.SampleOrchestratorServer.model.json.SubmissionStatusResponse;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorSummary;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorSummaryResponse;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorSummaryResponseResult;
import org.brapi.test.SampleOrchestratorServer.model.json.Status.MessageTypeEnum;
import org.brapi.test.SampleOrchestratorServer.model.json.WSMIMEDataTypes;
import org.brapi.test.SampleOrchestratorServer.service.SampleService;
import org.brapi.test.SampleOrchestratorServer.service.SampleSubmissionService;
import org.brapi.test.SampleOrchestratorServer.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

	private SampleService sampleService;
	private VendorService vendorService;
	private SampleSubmissionService sampleSubmissionService;

	@Autowired
	public MainController(SampleService sampleService, VendorService vendorService, SampleSubmissionService sampleSubmissionService) {
		this.sampleService = sampleService;
		this.vendorService = vendorService;
		this.sampleSubmissionService = sampleSubmissionService;
	}
	
	// Step 1: Send sample data
	@CrossOrigin
	@RequestMapping(value = "/api/samples", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.POST)
	public ResponseEntity<SamplesResponse> samplesPut(@Valid @RequestBody SamplesRequest request, 
			@RequestHeader(value="Authorization", required=false) String authorization)
			throws BrAPIServerException {
		List<Sample> data = sampleService.saveSamples(request);

		SamplesResponseResult result = new SamplesResponseResult();
		result.setData(data);
		SamplesResponse response = new SamplesResponse();
		response.setMetadata(new Metadata().addStatusItem(new Status().message("Sample stored successfully").messageType(MessageTypeEnum.INFO)));
		response.setResult(result);
		return new ResponseEntity<SamplesResponse>(response, HttpStatus.OK);
	}

	// Step 2: Choose a Vendor
	@CrossOrigin
	@RequestMapping(value = "/api/vendors", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.GET)
	public ResponseEntity<VendorSummaryResponse> vendorsGet(@RequestHeader(value="Authorization", required=false) String authorization) throws BrAPIServerException{
		
		List<VendorSummary> vendors = vendorService.getAllVendors();
		
		VendorSummaryResponseResult result = new VendorSummaryResponseResult();
		result.setData(vendors);
		
		VendorSummaryResponse response = new VendorSummaryResponse();
		response.setMetadata(new Metadata()
				.pagination(new MetadataPagination()
						.currentPage(0)
						.pageSize(100)
						.totalCount(1)
						.totalPages(1)));
		response.setResult(result);
		return new ResponseEntity<VendorSummaryResponse>(response, HttpStatus.OK);
		
	}
	
	// Step 3: Build plates
	@CrossOrigin
	@RequestMapping(value = "/api/submission", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.POST)
	public ResponseEntity<SubmissionResponse> submissionPost(@RequestBody SubmissionRequest request,
			@RequestHeader(value="Authorization", required=false) String authorization) throws BrAPIServerException{		
		// build plates
		SubmissionEntity submission = vendorService.buildSubmission(request);
		
		//return plate layout
		SubmissionResponseResult result = new SubmissionResponseResult();
		result.setSubmissionDbId(submission.getId());
		SubmissionResponse response = new SubmissionResponse();
		response.setMetadata(new Metadata());
		response.setResult(result);
		return new ResponseEntity<SubmissionResponse>(response, HttpStatus.OK);
		
	}
	
	// Step 4: Submit plates to Vendor
	@CrossOrigin
	@RequestMapping(value = "/api/submission/{submissionId}/submit", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.POST)
	public ResponseEntity<SubmissionStatusResponse> submissionSubmissionIdSubmitPost(@PathVariable("submissionId") String submissionId,
			@RequestBody String body,
			@RequestHeader(value="Authorization", required=false) String authorization) throws BrAPIServerException{
		
		sampleSubmissionService.submitSamples(submissionId);
		SubmissionStatusResponse submission = new SubmissionStatusResponse();
		
		return new ResponseEntity<SubmissionStatusResponse>(submission, HttpStatus.OK);
	}
}
