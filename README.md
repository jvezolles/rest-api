[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=jvezolles_rest-api&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=jvezolles_rest-api)

# rest-api

This project is developed with:

- OpenJDK 21 (https://jdk.java.net)
- Spring Boot (https://spring.io/projects/spring-boot)
- Maven (https://maven.apache.org)

## Prerequisites

You must install OpenJDK on your machine and define the `JAVA_HOME` environment variable.

This variable should point to your local OpenJDK installation.

You also need to add `%JAVA_HOME%\bin` to your system's Path environment variable.

## Build

To package the application, a Maven build is performed without any profile.

It is not necessary to install Maven locally; the tooling is included in the sources.

This build performs:

- Compilation of all project dependencies
- Execution of all unit tests
- Generation of the final WAR file

To launch the build:

- In a Windows command prompt, go to the `api-users` folder
- Execute the command: `.\mvnw clean install`

The result of the build can be found in the `target` folder of the `api-users` project.

This folder contains the WAR file with the application and its associated Javadoc.

## Test Coverage

By default, unit tests are executed with Maven commands.

Test coverage results can be viewed at `target\site\jacoco\index.html`.

Test coverage is only available **after a build**.

## Starting the Application

The application is started using Maven.

To launch the application:

- In a Windows command prompt, go to the `api-users` folder
- Execute the command: `.\mvnw spring-boot:run`

The application becomes available after Spring Boot has started.

## Project URLs

The APIs are accessible at the following addresses:

- Retrieve a user: `GET http://localhost:8080/user/<username>`
- Create a user: `POST http://localhost:8080/user`, with the user JSON in the request body
- Delete a user: `DELETE http://localhost:8080/user/<username>`

## Documentation

Code documentation is available in the project’s `target` folder at `target\apidocs\index.html`.  
This documentation is only available **after a build**.

API documentation is available via Swagger at:  
http://localhost:8080/swagger-ui.html.  
This documentation is only available **after the application has started**.
