package org.brapi.test.SampleOrchestratorServer.repository;

import java.io.IOException;
import java.util.Map;

import javax.persistence.AttributeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HashMapConverter implements AttributeConverter<Map<String, Object>, String> {
	private static Logger logger = LoggerFactory.getLogger(HashMapConverter.class);
	private ObjectMapper objectMapper;
	
	public HashMapConverter() {
		this.objectMapper = new ObjectMapper();
	}

    @Override
    public String convertToDatabaseColumn(Map<String, Object> customerInfo) {
 
        String attributesJSON = null;
        try {
            attributesJSON = objectMapper.writeValueAsString(customerInfo);
        } catch (final JsonProcessingException e) {
            logger.error("JSON writing error", e);
        }
 
        return attributesJSON;
    }
 
    @Override
    public Map<String, Object> convertToEntityAttribute(String customerInfoJSON) {
 
        Map<String, Object> attributes = null;
        try {
            attributes = objectMapper.readValue(customerInfoJSON, Map.class);
        } catch (final IOException e) {
            logger.error("JSON reading error", e);
        }
 
        return attributes;
    }

}
