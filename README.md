# employeeserv

## Application Overview
employeeserv is a spring boot rest application which would provide the CRUD operations for `Employee` resource.

There are four modules in this application
- employeeservApi - This module contains the interface.
	- `v1/schema/employee.json` defines the employee resource.
	- `v1/schema/address.json` - A JSON schema for address
	- `jsonschema2pojo-maven-plugin` is being used to create `Employee POJO` from json file.
	- `EmployeeResource.java` is the interface for CRUD operations on `Employee` resource.
		- GET `/v1/bfs/employees/{id}` endpoint is defined to fetch the resource.
- employeeservImplementation - This module contains the implementation for the rest endpoints.
	- `EmployeeResourceImpl.java` implements the `EmployeeResource` interface.
- employeeservFunctionalTests - This module would have the functional tests.
- customAnnotator - A custom annotator that could enhance the functionalities by adding JPA annotations to the pojos that have been generated.  Read more about the annotator [here](./customAnnotator/README.md).

## How to run the application
- Please have Maven version `3.3.3` & Java 8 on your system.
- Use command `mvn clean install` to build the project.
- Use command `mvn spring-boot:run` from `employeeservImplementation` folder to run the project.
- The app is configured to run on 8080 by default.

## API Docs

Swagger docs has been configured. To access -> http://localhost:8080/swagger-ui.html

## API Endpoints

###  Retreive all employee details
```
GET /v1/bfs/employees  

200 OK
Returns an array of employee objects. Can be empty as well.
```

### Add an employee
```
POST /v1/bfs/employees  

201 CREATED

** Note: All fields are required except for line2 under address. The size of strings should be between 1 and 255. 
{
    "first_name": "Mithun",
    "last_name": "Babu",
    "date_of_birth": "1994-01-01",		// format - yyyy-MM-dd
    "address": {
        "line1": "line1",
		"line2": "line2"				// optional field
        "city": "Bengaluru",
        "state": "Karnataka",
        "country": "India",
        "zip_code": "560062"
    }
}

400 BAD REQUEST
Occurs when invalid request body is passed. Respective messages are returned in the response.

500 INTERNAL SERVER ERROR
If any unexpected errors occur during creation of the employee
```

### Retreive employee details by id
```
GET /v1/bfs/employees/{id}

200 OK 
The respective employee is returned

404 NOT FOUND
If the employee with the given id doesn't exist
```
