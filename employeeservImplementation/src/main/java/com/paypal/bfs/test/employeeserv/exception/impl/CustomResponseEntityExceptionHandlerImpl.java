package com.paypal.bfs.test.employeeserv.exception.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.paypal.bfs.test.employeeserv.exception.CustomResponseEntityExceptionHandler;
import com.paypal.bfs.test.employeeserv.exception.EmployeeNotFoundException;

/**
 * Implementation of custom exception handler for the application
 * @author Mithun
 *
 */
@RestControllerAdvice
public class CustomResponseEntityExceptionHandlerImpl extends ResponseEntityExceptionHandler
		implements CustomResponseEntityExceptionHandler {

	private static final String VALIDATION_FAILED = "Validation failed";
	private static final String INVALID_INPUT = "Invalid input";
	private static final Logger logger = LoggerFactory.getLogger(CustomResponseEntityExceptionHandlerImpl.class);

	@Override
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
		ExceptionResponse resp = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		logger.error("Exception -> ", ex);
		return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Throwable rootCause = ex.getRootCause();
		ExceptionResponse resp = null;
		if (rootCause instanceof InvalidFormatException) {
			InvalidFormatException cause = (InvalidFormatException)rootCause;
			resp = new ExceptionResponse(new Date(), VALIDATION_FAILED, cause.getOriginalMessage());
		} else if (rootCause instanceof MismatchedInputException) {
			resp = new ExceptionResponse(new Date(), INVALID_INPUT, request.getDescription(false));
		} else if(rootCause instanceof JsonEOFException) {
			JsonEOFException cause = (JsonEOFException)rootCause;
			resp = new ExceptionResponse(new Date(), VALIDATION_FAILED, cause.getOriginalMessage());
		}
		if (resp == null) {
			resp = new ExceptionResponse(new Date(), VALIDATION_FAILED, ex.getMessage());
		}
		return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
	}
	
	@Override
	@ExceptionHandler(TransactionSystemException.class)
	public ResponseEntity<Object> handleTransactionSystemExceptions(TransactionSystemException ex, WebRequest request) {
		Throwable cause = ((TransactionSystemException) ex).getRootCause();
		ExceptionResponse resp = null;
		if (cause instanceof ConstraintViolationException) {
			Map<Object, Object> violationMap = new HashMap<>();
			 for(ConstraintViolation<?> constraintViolation: ((ConstraintViolationException) cause).getConstraintViolations()) {
				 String path = constraintViolation.getPropertyPath().toString();
				 String violation = constraintViolation.getMessage();
				 violationMap.put(path, violation);
			 }
			 if (violationMap.size() > 0) {
				 resp = new ExceptionResponse(new Date(), VALIDATION_FAILED, violationMap);
				 return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
			 }
	    }
		resp = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		logger.error("Exception -> ", ex);
		return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@ExceptionHandler(EmployeeNotFoundException.class)
	public ResponseEntity<Object> handleEmployeeNotFoundExceptions(EmployeeNotFoundException ex, WebRequest request) {
		ExceptionResponse resp = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		Map<Object, Object> errorMap = new HashMap<>();
		for (FieldError fieldError: fieldErrors) {
			String field = fieldError.getField();
			String error = fieldError.getDefaultMessage();
			errorMap.put(field, error);
		}
		ExceptionResponse resp = new ExceptionResponse(new Date(), VALIDATION_FAILED, errorMap);
		return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
	}
}
