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
		errors.put("status", HttpStatus.BAD_REQUEST.toString());
		errors.put("message", ve.getMessage().isEmpty() ? "Missing Object ID" : Arrays.asList(ve.getMessage().split("\\s*,\\s*")));

		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
 
	}
	
	@ExceptionHandler(Exception.class)
	public @ResponseBody Object handleGeneralException(HttpServletRequest request, Exception e) throws Exception {
		log.info("...caught undefined exception...");

		Map<String, String> errors = new HashMap<>();
		errors.put("status", HttpStatus.BAD_REQUEST.toString());
		errors.put("message", e.getMessage());

		e.printStackTrace();

		return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
}
