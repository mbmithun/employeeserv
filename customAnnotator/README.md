# customAnnotator

## Overview

The employeeserv app uses the `jsonschema2pojo-maven-plugin` is being used to create pojos from json files. It has several limitations.
When these pojos need to be integrated with Spring Data JPA, there is no direct approach. There are a few approaches one could take - 

1. A custom annotator that could enhance the functionalities by adding persistance apis to the pojos that have been generated.  
2. Use an orm.xml file and have it under the *src/main/resources/META-INF* folder. The orm.xml file will contain the entity mapping
	and any relation related information. The disadvantage with this approach is that every time a change is made in the schema,
	this file also needs change. The first solution solves this problem.
3. Not use spring data jpa and use direct sql queries. This will only introduce lots of code and queries to be managed.

## Integrating this project

This is a maven project. To get it working, add it as a dependency in the project where the `jsonschema2pojo-maven-plugin` is used as shown  

```
		<plugins>
            <plugin>
                <groupId>org.jsonschema2pojo</groupId>
                <artifactId>jsonschema2pojo-maven-plugin</artifactId>
                <version>1.0.2</version>
                <dependencies>
                	<dependency>
                		<groupId>com.paypal.bfs.test</groupId>
                		<artifactId>customAnnotator</artifactId>
                		<version>0.0.1-SNAPSHOT</version>
                	</dependency>
                	 <dependency>
			            <groupId>org.springframework.boot</groupId>
			            <artifactId>spring-boot-starter-data-jpa</artifactId>
			            <scope>compile</scope>
			            <version>2.1.9.RELEASE</version>
			        </dependency>
                </dependencies>
                <configuration>
                    <sourceDirectory>${project.basedir}/src/main/resources/v1/schema</sourceDirectory>
                    <targetPackage>com.paypal.bfs.test.employeeserv.api.model</targetPackage>
                    <useCommonsLang3>true</useCommonsLang3>
                    <includeHashcodeAndEquals>false</includeHashcodeAndEquals>
                    <includeJsr303Annotations>true</includeJsr303Annotations>
                    <includeAdditionalProperties>false</includeAdditionalProperties>
                    <includeConstructors>true</includeConstructors>
                    <includeAllPropertiesConstructor>false</includeAllPropertiesConstructor>
                    <customAnnotator>com.paypal.bfs.customAnnotator.CustomAnnotator</customAnnotator>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>generate</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
```

## Usage

Consider an employee schema written in the JSON file. The description of all the new capabilities are added as comments in the JSON below.   

```
{
  "title": "Employee resource",
  "description": "Employee resource object",
  "type": "object",
  "entity": "true",    # Adding this field will add the Entity annotation to the Employee POJO generated.
  "properties": {
    "id": {
      "description": "employee id",
      "type": "integer",
      "id": true,		# Adding this field will add the Id annotation to the respective field in the Employee POJO generated.
      "generatedValue": true  # Adding this field will add the GeneratedValue annotation to the respective field. This will only work if the id field is set.
      "jsonIgnore": true	# If this field is set to true, the respective field will not be considered during serialization
    },
    "name": {
      "description": "last name",
      "type": "string",
      "minLength": 1,
      "maxLength": 255
    },
    "address": {
    	"description": "address of the employee",
    	"$ref": "address.json"
    }
  },
  # This is a property added at the schema level. The properties under this section should be part of the
  # properties mentioned above. If a relation such as OneToOne, OneToMany, ManyToOne or ManyToMany should
  # be added to this property, the respective annotation will be added. The cascade type can also be chosen(optional).
  "entityToEntityMapping" :{
    	"address": {
    		"relation": "OneToOne",
    		"cascadeType": "all"
    	}
   }
}
```

