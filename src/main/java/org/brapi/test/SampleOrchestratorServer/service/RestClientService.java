package org.brapi.test.SampleOrchestratorServer.service;

import java.net.URI;

import org.brapi.test.SampleOrchestratorServer.model.entity.VendorEntity;
import org.brapi.test.SampleOrchestratorServer.model.json.SamplesResponse;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorOrderDetailsResponse;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorOrderRequest;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorOrderResponse;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorOrderResultsResponse;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorOrderStatusResponse;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorSpecification;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorSpecificationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RestClientService {

    private static Logger logger = LoggerFactory.getLogger(RestClientService.class);

	private static String AUTH_HEADER = "Authorization";
	public static String BASE_URI = "https://test-server.brapi.org/brapi/v1";
	public static String POST_VENDOR_ORDERS = "/vendor/orders";
	public static String POST_VENDOR_SAMPLES = "/vendor/plates";
	public static String GET_VENDOR_ORDERS_STATUS = "/vendor/orders/{orderId}/status";
	public static String GET_VENDOR_ORDERS_RESULTS = "/vendor/orders/{orderId}/results";
	public static String GET_VENDOR_ORDERS_FOR_SUBMISSIONID = "/vendor/orders";
	public static String GET_EXTERNAL_SOURCE_SAMPLES = "/samples";

	public VendorOrderResponse postVendorSamples(String baseURL, String authToken, VendorOrderRequest request) {
		URI uri = URI.create(baseURL + POST_VENDOR_SAMPLES);
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set(AUTH_HEADER, authToken);
		HttpEntity<VendorOrderRequest> entity = new HttpEntity<VendorOrderRequest>(request, headers);
		VendorOrderResponse response = null;
		ObjectMapper mapper = new ObjectMapper();
		try { 
			System.out.println(mapper.writeValueAsString(request));
			ResponseEntity<VendorOrderResponse> raw = restTemplate.exchange(uri, HttpMethod.POST, entity, VendorOrderResponse.class);
			logger.debug("response status: " + raw.getStatusCodeValue());
			response = raw.getBody();
		}catch (RestClientException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return response;
	}
	public VendorOrderResponse postVendorOrders(String baseURL, String authToken, VendorOrderRequest vendorOrderRequest) {
		URI uri = URI.create(baseURL + POST_VENDOR_ORDERS);
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set(AUTH_HEADER, authToken);
		HttpEntity<VendorOrderRequest> entity = new HttpEntity<VendorOrderRequest>(vendorOrderRequest, headers);
		VendorOrderResponse response = null;
		try { 
			ResponseEntity<VendorOrderResponse> raw = restTemplate.exchange(uri, HttpMethod.POST, entity, VendorOrderResponse.class);
			logger.debug("response status: " + raw.getStatusCodeValue());
			response = raw.getBody();
		}catch (RestClientException e) {
			e.printStackTrace();
		}

		return response;
	}

	public VendorOrderStatusResponse getVendorOrdersStatus(String baseURL, String authToken, String orderId) {
		URI uri = URI.create(baseURL + GET_VENDOR_ORDERS_STATUS.replace("{orderId}", orderId));
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set(AUTH_HEADER, authToken);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		VendorOrderStatusResponse response = null;
		try { 
			ResponseEntity<VendorOrderStatusResponse> raw = restTemplate.exchange(uri, HttpMethod.GET, entity, VendorOrderStatusResponse.class);
			logger.debug("response status: " + raw.getStatusCodeValue());
			response = raw.getBody();
		}catch (RestClientException e) {
			// TODO: handle exception
		}
		return response;
	}

	public VendorOrderResultsResponse getVendorOrdersResults(String baseURL, String authToken, String orderId) {
		URI uri = URI.create(baseURL + GET_VENDOR_ORDERS_RESULTS.replace("{orderId}", orderId));
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set(AUTH_HEADER, authToken);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		VendorOrderResultsResponse response = null;
		try { 
			ResponseEntity<VendorOrderResultsResponse> raw = restTemplate.exchange(uri, HttpMethod.GET, entity, VendorOrderResultsResponse.class);
			logger.debug("response status: " + raw.getStatusCodeValue());
			response = raw.getBody();
		}catch (RestClientException e) {
			// TODO: handle exception
		}
		return response;
	}

	public String getGenotypeDBLoadStatus(String baseURL, String authToken, String genotypeDB_OrderId) {
		return "recieved";
	}
	
	public VendorSpecification getVendorSpecifications(String authToken, VendorEntity vendorEntity) {	
		VendorSpecification spec = null;	
		if (vendorEntity != null) {			
			String uri = vendorEntity.getVendorBaseURL();
			
			if(!uri.endsWith("/")) {
				uri = uri + "/";
			}
			if(uri.endsWith("brapi/v1/")){
				uri = uri + "vendor/specifications";
			}else {
				uri = uri + "brapi/v1/vendor/specifications";
			}
			
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.set(AUTH_HEADER, authToken);
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			try { 
				ResponseEntity<VendorSpecificationResponse> raw = restTemplate.exchange(uri, HttpMethod.GET, entity, VendorSpecificationResponse.class);
				logger.debug("response status: " + raw.getStatusCodeValue());
				spec = raw.getBody().getResult();
			}catch (RestClientException e) {
				logger.error("call to uri failed: " + uri);
				logger.error(e.getMessage());
				//e.printStackTrace();
			}
		}
		return spec;
	}
	
	public VendorOrderDetailsResponse getVendorOrdersForSubmission(String baseURL, String authToken,
			String submissionId) {
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl(baseURL + GET_VENDOR_ORDERS_FOR_SUBMISSIONID)
		        .queryParam("submissionId", submissionId);
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set(AUTH_HEADER, authToken);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		VendorOrderDetailsResponse response = null;
		try { 
			ResponseEntity<VendorOrderDetailsResponse> raw = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, VendorOrderDetailsResponse.class);
			logger.debug("response status: " + raw.getStatusCodeValue());
			response = raw.getBody();
		}catch (RestClientException e) {
			// TODO: handle exception
		}
		return response;
	}
	
	public SamplesResponse getExternalSourceSamples(String baseURL, String authToken) {
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl(baseURL + GET_EXTERNAL_SOURCE_SAMPLES)
		        .queryParam("pageSize", "1");
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set(AUTH_HEADER, authToken);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		SamplesResponse response = null;
		try { 
			ResponseEntity<SamplesResponse> raw = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, SamplesResponse.class);
			logger.debug("response status: " + raw.getStatusCodeValue());
			response = raw.getBody();
		}catch (RestClientException e) {
			// TODO: handle exception
		}
		return response;
	}

}
