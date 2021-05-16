package com.paypal.bfs.test.employeeserv.impl;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.paypal.bfs.test.employeeserv.api.EmployeeResource;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.service.EmployeeService;

/**
 * Implementation class for employee resource.
 */
@RestController
public class EmployeeResourceImpl implements EmployeeResource{
	
	@Autowired
	private EmployeeService empService;

	@Override
    public ResponseEntity<Employee> employeeGetById(String id) {
        Employee employee = empService.findEmployeeById(id);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

	@Override
	public ResponseEntity<Object> createEmployee(Employee employee) {
		Employee savedEmp = empService.createEmployee(employee);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedEmp.getId()).toUri();
		Map<String, String> body = new HashMap<>();
		body.put("link", location.toString());
		return ResponseEntity.created(location).body(body);
	}

	@Override
	public List<Employee> getAllEmployees() {
		return empService.findAll();
	}
}
