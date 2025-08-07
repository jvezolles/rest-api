[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=jvezolles_rest-api&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=jvezolles_rest-api)

# rest-api

This project is developed with:

- OpenJDK 21 (https://jdk.java.net)
- Spring Boot (https://spring.io/projects/spring-boot)
- Maven (https://maven.apache.org)

## Prerequisites

You must install:
- OpenJDK on your machine and define the `JAVA_HOME` environment variable
- Maven on your machine and define the `MAVEN_HOME` environment variable

These variables should point to your local OpenJDK and Maven installation.

You also need to add `%JAVA_HOME%\bin` and `%MAVEN_HOME%\bin` to your system's Path environment variable.

## Build

To package the local application, a Maven build is performed without any profile.

This build performs:

- Compilation of all project dependencies
- Execution of all unit tests
- Generation of the final JAR file

To launch the build:

- In a Windows command prompt, go to the `rest-api` folder
- Execute the command: `mvn clean install`

The result of the build can be found in the `target` folder of the `rest-api` project.

This folder contains the WAR file with the application and its associated Javadoc.

## Test Coverage

By default, unit tests are executed with Maven commands.

Test coverage results can be viewed at `target\site\jacoco\index.html`.

Test coverage is only available **after a build**.

## Starting the Application

The application is started using Maven.

To launch the application:

- In a Windows command prompt, go to the `rest-api` folder
- Execute the command: `mvn spring-boot:run`

The application becomes available after Spring Boot has started.

## Project URLs

The APIs are accessible at the following addresses:

- Retrieve a user: `GET http://localhost:8080/user/<username>`
- Create a user: `POST http://localhost:8080/user`, with the user JSON in the request body
- Delete a user: `DELETE http://localhost:8080/user/<username>`
