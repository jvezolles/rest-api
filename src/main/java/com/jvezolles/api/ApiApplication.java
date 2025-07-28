package com.jvezolles.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Main class, used to launch spring boot
 *
 * @author Vezolles
 */
@SpringBootApplication
public class ApiApplication extends SpringBootServletInitializer {

    /**
     * Main method, used to launch spring boot application
     *
     * @param args arguments passed to application
     */
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    /**
     * Configure the application
     *
     * @param application a builder for the application context
     * @return the application builder
     * @see SpringApplicationBuilder
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ApiApplication.class);
    }

}
