package com.paypal.bfs.test.employeeserv.exception;

/**
 * A wrapper used when an employee is not found for given id
 * @author Mithun
 *
 */
public class EmployeeNotFoundException extends RuntimeException {

	public EmployeeNotFoundException(String message) {
		super(message);
	}
}
