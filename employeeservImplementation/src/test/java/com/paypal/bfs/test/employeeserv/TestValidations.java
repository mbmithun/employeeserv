package com.paypal.bfs.test.employeeserv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;

@RunWith(SpringRunner.class)
public class TestValidations {

	private static final Logger logger = LoggerFactory.getLogger(TestValidations.class);
	private static ValidatorFactory validatorFactory;
	private static Validator validator;
	private Employee employee;
	
	@BeforeClass
	public static void createValidator() {
		validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}
	
	@Before
	public void before() throws ParseException {
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse("1994-01-01");
		employee = new Employee(1, "John", "Micheal", date, 
				new Address(1, "line1", "line2", "city", "state", "country",560023));
	}
	
	@AfterClass
	public static void close() {
		validatorFactory.close();
	}
	
	@Test
	public void testNullValuesInField() {
		employee.setLastName(null);
		employee.setAddress(null);
		employee.setDateOfBirth(null);
		employee.setAddress(new Address(1, null, "line2", "city", "state", "country",560023));
		
		Set<String> keyViolations = new HashSet<>(Arrays.asList("address", "lastName", "dateOfBirth", "address.line1"));
		Set<ConstraintViolation<Employee>> constraintViolations = validator.validate( employee );
		logger.info("Number of violations: {}", constraintViolations.size());
		assertEquals(3, constraintViolations.size());

		for (ConstraintViolation<Employee> constraintViolation: constraintViolations) {
			String path = constraintViolation.getPropertyPath().toString();
			String violation = constraintViolation.getMessage();
			logger.info("Test for {}", path);
			assertTrue(keyViolations.contains(path));
			assertEquals("must not be null", violation);
		}
	}

	@Test
	public void testSizeOfField() {
		employee.setFirstName(""); // Empty String
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<500; i++) {
			sb.append("a");
		}
		employee.setLastName(sb.toString()); // Long string

		Set<ConstraintViolation<Employee>> constraintViolations = validator.validate( employee );
		Set<String> keyViolations = new HashSet<>(Arrays.asList("firstName", "lastName"));
		logger.info("Number of violations: {}", constraintViolations.size());
		assertEquals(2, constraintViolations.size());

		for (ConstraintViolation<Employee> constraintViolation: constraintViolations) {
			String path = constraintViolation.getPropertyPath().toString();
			String violation = constraintViolation.getMessage();
			logger.info("Test for {}", path);
			assertTrue(keyViolations.contains(path));
			assertEquals("size must be between 1 and 255", violation);
		}
	}
}
