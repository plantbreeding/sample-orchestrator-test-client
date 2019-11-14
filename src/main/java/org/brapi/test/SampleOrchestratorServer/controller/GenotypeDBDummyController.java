package org.brapi.test.SampleOrchestratorServer.controller;

import javax.validation.Valid;

import org.brapi.test.SampleOrchestratorServer.exceptions.BrAPIServerException;
import org.brapi.test.SampleOrchestratorServer.model.json.SamplesRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenotypeDBDummyController {

	@CrossOrigin
	@RequestMapping(value = "/api/genodummy/samples", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.POST)
	public ResponseEntity<String> samplesPOST(@Valid @RequestBody SamplesRequest request,
			@RequestHeader(value = "Authorization", required = false) String authorization)
			throws BrAPIServerException {

		return new ResponseEntity<String>("", HttpStatus.OK);
	}
}
