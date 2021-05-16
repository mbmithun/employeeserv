package com.paypal.bfs.test.employeeserv.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.exception.EmployeeNotFoundException;

/**
 * A service to handle operations with respect to employees
 * @author Mithun
 *
 */
public interface EmployeeService {
	
	/**
	 * Find all employees in the database
	 * @return a list containing all the employees available
	 */
	List<Employee> findAll();
	
	/**
	 * Given an employee id, finds the corresponding employee
	 * @param id The employee id
	 * @return The employee object if present
	 * 
	 * @throws EmployeeNotFoundException if no employee exists with given id
	 */
	Employee findEmployeeById(String id) throws EmployeeNotFoundException;

	/**
	 * Create an employee with the given employee object
	 * @param employee The employee object to insert
	 * @return 
	 */
	Employee createEmployee(Employee employee);
}
