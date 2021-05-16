package com.paypal.bfs.test.employeeserv.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.exception.EmployeeNotFoundException;
import com.paypal.bfs.test.employeeserv.repository.EmployeeRepository;
import com.paypal.bfs.test.employeeserv.service.EmployeeService;

/**
 * An implementation of the EmployeeService
 * @author Mithun
 *
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {
	
	@Autowired
	private EmployeeRepository empRepository;

	@Override
	public List<Employee> findAll() {
		List<Employee> employees = new ArrayList<>();
		empRepository.findAll().forEach(employees::add);
		return employees;
	}

	@Override
	public Employee findEmployeeById(String empId) throws EmployeeNotFoundException {
		
		try {
			Integer id = Integer.valueOf(empId);
			return empRepository.findById(id).orElseThrow();
		} catch (Exception ex) {
			throw new EmployeeNotFoundException("No employee found with id -> " + empId);
		}
	}

	@Override
	public Employee createEmployee(Employee employee) {
		return empRepository.save(employee);		
	}
}
