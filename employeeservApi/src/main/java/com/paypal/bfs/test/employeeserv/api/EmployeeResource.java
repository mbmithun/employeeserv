package com.paypal.bfs.test.employeeserv.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.paypal.bfs.test.employeeserv.api.model.Employee;

import io.swagger.annotations.ApiOperation;

/**
 * Interface for employee resource operations.
 */
public interface EmployeeResource {

    /**
     * Retrieves the {@link Employee} resource by id.
     *
     * @param id employee id.
     * @return {@link Employee} resource.
     */
    @GetMapping("/v1/bfs/employees/{id}")
	@ApiOperation(value = "Retreive employee details by id", 
		notes = "Provide an id to look up a specific employee's details", 
		response = Employee.class)
    ResponseEntity<Employee> employeeGetById(@PathVariable("id") String id);
    
    /**
     * Retrieves all employees
     * @return Returns all the employee objects as a List
     */
    @GetMapping("/v1/bfs/employees")
    @ApiOperation(value = "Retreive all employee details", 
		notes = "Look up details of every employee", 
		response = List.class)
    List<Employee> getAllEmployees();

    /**
     * Creates an employee with the given details
     * @param employee An object containing all employee details
     */
    @PostMapping("/v1/bfs/employees")
    @ApiOperation(value = "Add an employee", 
		notes = "Provide all required details of the employee")
    ResponseEntity<Object> createEmployee(@Valid @RequestBody Employee employee);
}
