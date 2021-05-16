package com.paypal.bfs.test.employeeserv.resourceTest;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.exception.EmployeeNotFoundException;
import com.paypal.bfs.test.employeeserv.repository.EmployeeRepository;
import com.paypal.bfs.test.employeeserv.service.EmployeeService;

@RunWith(SpringRunner.class)
@WebMvcTest
public class EmployeeResourceTest {

	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	EmployeeRepository empRepository;
	
	@MockBean
	EmployeeService empService;
	
	@Test
	public void initialTestEmpty() throws Exception {
		
		Mockito.when(empRepository.findAll()).thenReturn(Collections.emptyList());
		
		MvcResult mvcResult = mockMvc.perform(
				MockMvcRequestBuilders.get("/v1/bfs/employees")
				.accept(MediaType.APPLICATION_JSON)
				).andReturn();
		
		assertEquals(200, mvcResult.getResponse().getStatus());
	}
	
	@Test
	public void testInvalidEmployeeGetById() throws Exception {
		Mockito.when(empService.findEmployeeById("blah")).thenThrow(EmployeeNotFoundException.class);
		
		MvcResult mvcResult = mockMvc.perform(
				MockMvcRequestBuilders.get("/v1/bfs/employees/blah")
				.accept(MediaType.APPLICATION_JSON)
				).andReturn();
		
		MockHttpServletResponse response = mvcResult.getResponse();
		int statusCode = response.getStatus();
		assertEquals(404, statusCode);
	}
	
	@Test
	public void testValidEmployeeGetById() throws Exception {
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse("1994-01-01");
		Employee employee = new Employee(1, "John", "Micheal", date, 
				new Address(1, "line1", "line2", "city", "state", "country",560023));
		Mockito.when(empService.findEmployeeById("1")).thenReturn(employee);
		
		MvcResult mvcResult = mockMvc.perform(
				MockMvcRequestBuilders.get("/v1/bfs/employees/1")
				.accept(MediaType.APPLICATION_JSON)
				).andReturn();
		
		MockHttpServletResponse response = mvcResult.getResponse();
		int statusCode = response.getStatus();
		assertEquals(200, statusCode);
	}
	
	@Test
	public void testNoContentCreateEmployee() throws Exception {
		
		MvcResult mvcResult = mockMvc.perform(
				MockMvcRequestBuilders.post("/v1/bfs/employees/").content("")
				.accept(MediaType.APPLICATION_JSON)
				).andReturn();

		assertEquals(400, mvcResult.getResponse().getStatus());
	}
}
