package org.brapi.test.SampleOrchestratorServer.service;

import java.net.URI;

import org.brapi.test.SampleOrchestratorServer.model.entity.VendorEntity;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorOrderRequest;
import org.brapi.test.SampleOrchestratorServer.model.json.VendorOrderResponse;
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

@Service
public class RestClientService {

    private static Logger logger = LoggerFactory.getLogger(RestClientService.class);

	private static String AUTH_HEADER = "Authorization";
	public static String BASE_URI = "https://test-server.brapi.org/brapi/v1";
	public static String POST_VENDOR_ORDERS = "/vendor/orders";
	public static String GET_VENDOR_ORDERS_STATUS = "/vendor/orders/{orderId}/status";

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
			// TODO: handle exception
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

	public String getGenotypeDBLoadStatus(String genotypeDB_OrderId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public VendorSpecification getVendorSpecifications(String authToken, VendorEntity vendorEntity) {	
		VendorSpecification spec = null;	
		if (vendorEntity != null) {			
			String uri = vendorEntity.getVendorBaseURL();
			
			if(!uri.endsWith("/")) {
				uri = uri + "/";
			}
			if(uri.endsWith("brapi/v1/")){
				uri = uri + "vendor/specifications/";
			}else {
				uri = uri + "brapi/v1/vendor/specifications/";
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
				// TODO: handle exception
			}
		}
		return spec;
	}
}
