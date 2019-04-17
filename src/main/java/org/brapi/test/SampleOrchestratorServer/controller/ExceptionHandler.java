package org.brapi.test.SampleOrchestratorServer.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authz.AuthorizationException;
import org.brapi.test.SampleOrchestratorServer.exceptions.BrAPIServerException;
import org.brapi.test.SampleOrchestratorServer.model.json.Metadata;
import org.brapi.test.SampleOrchestratorServer.model.json.Status;
import org.brapi.test.SampleOrchestratorServer.model.json.Status.MessageTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler{
    private static Logger log = LoggerFactory.getLogger(ExceptionHandler.class);
    
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
	    String error = ex.getParameterName() + " parameter is missing";
	    
	    return buildErrorResponse(HttpStatus.BAD_REQUEST, error);
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(value = {BrAPIServerException.class})
	protected ResponseEntity<Object> handleBrAPIException(BrAPIServerException ex, WebRequest request){
		String message = ex.getResponseMessage().replaceAll("\"", "\'");
	    return new ResponseEntity<Object>("\"" + message + "\"", ex.getResponseStatus());		
	}
	
	private ResponseEntity<Object> buildErrorResponse(HttpStatus code, String message){
	    Status statusRes = new Status();
	    statusRes.setMessageType(MessageTypeEnum.ERROR);
	    statusRes.setMessage(message);
	    
	    Metadata apiError = new Metadata();
	    apiError.setStatus(new ArrayList<>());
	    apiError.getStatus().add(statusRes);
	    
	    return new ResponseEntity<Object>(apiError, code);
	}
	
    @org.springframework.web.bind.annotation.ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleException(AuthorizationException e, Model model) {

        // you could return a 404 here instead (this is how github handles 403, so the user does NOT know there is a
        // resource at that location)
        log.debug("AuthorizationException was thrown", e);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", HttpStatus.FORBIDDEN.value());
        map.put("message", "No message available");
        model.addAttribute("errors", map);

        return "error";
    }
}
