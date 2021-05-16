package com.paypal.bfs.test.employeeserv.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.context.request.WebRequest;

/**
 * A custom exception handler for the application
 * @author Mithun
 *
 */
public interface CustomResponseEntityExceptionHandler {

	/**
	 * A handler for all exceptions if they go uncaught with pre-existing handlers
	 * @param ex The exception
	 * @param request The web request object that came in
	 * @return The response entity object with respective status and error details
	 */
	ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request);
	
	/**
	 * A handler for EmployeeNotFound exceptions
	 * @param ex The exception
	 * @param request The web request object that came in
	 * @return The response entity object with respective status and error details
	 */
	ResponseEntity<Object> handleEmployeeNotFoundExceptions(EmployeeNotFoundException ex, WebRequest request);

	/**
	 * A handler for all TransactionSystem exceptions. Could occur when executing db queries
	 * @param ex The exception
	 * @param request The web request object that came in
	 * @return The response entity object with respective status and error details
	 */
	ResponseEntity<Object> handleTransactionSystemExceptions(TransactionSystemException ex, WebRequest request);
}
