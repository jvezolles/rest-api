# This Dockerfile is designed to build and run a Spring Boot application using Maven.
# It uses the official Maven image with the Eclipse Temurin JDK.
# The application will run as a non-root user.

# ===========================
# Step 1: Build with Maven
# ===========================
ARG MAVEN_VERSION=3.9.11
ARG JAVA_VERSION=21
FROM maven:${MAVEN_VERSION}-eclipse-temurin-${JAVA_VERSION} AS builder

# Set the default locale to UTF-8 to ensure proper character encoding.
ENV LANG=C.UTF-8

# Set the timezone to UTC to avoid issues with time-related operations.
ENV TZ=UTC

# Create a non-root user
RUN useradd -ms /bin/bash appuser

# Switch to the non-root user
USER appuser

# Working directory
WORKDIR /app

# Prepare the Maven cache
RUN mkdir -p /home/appuser/.m2 && chown -R appuser:appuser /home/appuser/.m2

# Copy only the pom.xml to leverage dependency caching
COPY --chown=root:root --chmod=755 pom.xml ./

# Download Maven dependencies
RUN mvn -B -Dmaven.repo.local=/home/appuser/.m2/repository dependency:go-offline

# Copy the rest of the project
COPY --chown=root:root --chmod=755 src ./src

# Build the project and create the jar
RUN mvn -B -Dmaven.repo.local=/home/appuser/.m2/repository clean package -Dmaven.test.skip=true

# ===========================
# Step 2: Runtime
# ===========================
FROM eclipse-temurin:${JAVA_VERSION}-jre

# Set the default locale to UTF-8 to ensure proper character encoding.
ENV LANG=C.UTF-8

# Set the timezone to UTC to avoid issues with time-related operations.
ENV TZ=UTC

# Create a non-root user for running the application
RUN useradd -ms /bin/bash appuser
USER appuser

WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose port (Spring Boot default)
EXPOSE 8080

# Start the application
ENTRYPOINT ["java","-jar","app.jar"]
