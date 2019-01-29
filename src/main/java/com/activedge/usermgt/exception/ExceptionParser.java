package com.activedge.usermgt.exception;


import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 */
@Slf4j
@ControllerAdvice
public class ExceptionParser {
	
	@ExceptionHandler(ValidationException.class)
	public @ResponseBody Object handleCustomException(ValidationException ve, HttpServletRequest request) {
		log.info("...caught validation exception...");

		Map<String, Object> errors = new HashMap<>();
		errors.put("status", HttpStatus.BAD_REQUEST.value());
		errors.put("message", ve.getMessage().isEmpty() ? "Missing Object ID" : Arrays.asList(ve.getMessage().split("\\s*,\\s*")));

		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
 
	}

	@ExceptionHandler(value
            = { java.lang.RuntimeException.class, ActivityRequiredException.class })
	public @ResponseBody Object handleActivityRequiredException(java.lang.RuntimeException ar, HttpServletRequest request) {
		log.info("...caught action required exception...");
        Throwable t = ar.getCause();

        Map<String, Object> errors = new HashMap<>();

        if(t instanceof ActivityRequiredException) {
            errors.put("status", HttpStatus.ACCEPTED.value());
            errors.put("message", ar.getLocalizedMessage());
            return new ResponseEntity<>(errors, HttpStatus.ACCEPTED);
        } else if(t instanceof ValidationException) {
            errors.put("status", HttpStatus.BAD_REQUEST.value());
            errors.put("message", t.getMessage());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        };

		return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	@ExceptionHandler(value={javax.persistence.RollbackException.class, Exception.class})
	public @ResponseBody Object handleGeneralException(HttpServletRequest request, Exception e) throws Exception {
		log.info("...caught generic exception...");

		Map<String, Object> errors = new HashMap<>();
		errors.put("status", HttpStatus.BAD_REQUEST.value());
		errors.put("message", e.getMessage());

		e.printStackTrace();

		return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
}
