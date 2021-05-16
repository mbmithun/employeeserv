package com.paypal.bfs.test.employeeserv.exception.impl;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A generic bean for exceptions
 * @author Mithun
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionResponse {

	private Date timestamp;
	private String message;
	private String detail;
	private Map<Object, Object> errors;
	
	public ExceptionResponse(Date timestamp, String message, String detail) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.detail = detail;
	}

	public ExceptionResponse(Date timestamp, String message, Map<Object, Object> errors) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.errors = errors;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

	public String getDetail() {
		return detail;
	}

	public Map<Object, Object> getErrors() {
		return errors;
	}
}
