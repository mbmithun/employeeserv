package com.paypal.bfs.test.employeeserv.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paypal.bfs.test.employeeserv.api.model.Employee;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer>{
}
