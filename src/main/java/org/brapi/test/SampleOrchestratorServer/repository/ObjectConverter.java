package org.brapi.test.SampleOrchestratorServer.repository;

import java.io.IOException;
import javax.persistence.AttributeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectConverter implements AttributeConverter<Object, String> {
	private static Logger logger = LoggerFactory.getLogger(ObjectConverter.class);
	private ObjectMapper objectMapper;
	
	public ObjectConverter() {
		this.objectMapper = new ObjectMapper();
	}

    @Override
    public String convertToDatabaseColumn(Object customerInfo) {
 
        String attributesJSON = null;
        try {
            attributesJSON = objectMapper.writeValueAsString(customerInfo);
        } catch (final JsonProcessingException e) {
            logger.error("JSON writing error", e);
        }
 
        return attributesJSON;
    }
 
    @Override
    public Object convertToEntityAttribute(String customerInfoJSON) {
 
        Object attributes = null;
        try {
            attributes = objectMapper.readValue(customerInfoJSON, Object.class);
        } catch (final IOException e) {
            logger.error("JSON reading error", e);
        }
 
        return attributes;
    }

}
