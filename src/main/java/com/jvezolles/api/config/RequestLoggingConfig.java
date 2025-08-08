package com.jvezolles.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;

/**
 * Configuration spring boot for request logging
 *
 * @author Vezolles
 */
@Configuration
@Aspect
@Slf4j
@AllArgsConstructor
public class RequestLoggingConfig {

    /**
     * Request of incoming request
     */
    private HttpServletRequest request;

    /**
     * Mapper user to convert Object to String
     */
    private ObjectMapper mapper;

    /**
     * Log GET incoming request
     *
     * @param joinPoint join point used with aspect
     * @return result of method execution
     * @throws Throwable if an error occurs
     */
    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public Object logAroundGetMapping(ProceedingJoinPoint joinPoint) throws Throwable {

        Object result = null;

        // Log incoming request
        log.info("-> GET {}", request.getRequestURL());
        long startTime = System.currentTimeMillis();

        try {
            // Proceed method
            result = joinPoint.proceed();
            return result;

        } finally {
            long endtime = System.currentTimeMillis();

            // Log end incoming request
            log.info("<- GET {} in {}ms with body : {}", request.getRequestURL(), endtime - startTime, mapper.writeValueAsString(result));
        }
    }

    /**
     * Log POST incoming request
     *
     * @param joinPoint join point used with aspect
     * @return result of method execution
     * @throws Throwable if an error occurs
     */
    @Around("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public Object logAroundPostMapping(ProceedingJoinPoint joinPoint) throws Throwable {

        Object result = null;
        String body = request.getReader().lines().collect(Collectors.joining());

        // Log incoming request
        log.info("-> POST {} with body : {}", request.getRequestURL(), body);
        long startTime = System.currentTimeMillis();

        try {
            // Proceed method
            result = joinPoint.proceed();
            return result;

        } finally {
            long endtime = System.currentTimeMillis();

            // Log end incoming request
            log.info("<- POST {} in {}ms with body : {}", request.getRequestURL(), endtime - startTime, mapper.writeValueAsString(result));
        }
    }

    /**
     * Log PATCH incoming request
     *
     * @param joinPoint join point used with aspect
     * @return result of method execution
     * @throws Throwable if an error occurs
     */
    @Around("@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    public Object logAroundPatchMapping(ProceedingJoinPoint joinPoint) throws Throwable {

        Object result = null;
        String body = request.getReader().lines().collect(Collectors.joining());

        // Log incoming request
        log.info("-> PATCH {} with body : {}", request.getRequestURL(), body);
        long startTime = System.currentTimeMillis();

        try {
            // Proceed method
            result = joinPoint.proceed();
            return result;

        } finally {
            long endtime = System.currentTimeMillis();

            // Log end incoming request
            log.info("<- PATCH {} in {}ms with body : {}", request.getRequestURL(), endtime - startTime, mapper.writeValueAsString(result));
        }
    }

    /**
     * Log PUT incoming request
     *
     * @param joinPoint join point used with aspect
     * @return result of method execution
     * @throws Throwable if an error occurs
     */
    @Around("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public Object logAroundPutMapping(ProceedingJoinPoint joinPoint) throws Throwable {

        Object result = null;
        String body = request.getReader().lines().collect(Collectors.joining());

        // Log incoming request
        log.info("-> PUT {} with body : {}", request.getRequestURL(), body);
        long startTime = System.currentTimeMillis();

        try {
            // Proceed method
            result = joinPoint.proceed();
            return result;

        } finally {
            long endtime = System.currentTimeMillis();

            // Log end incoming request
            log.info("<- PUT {} in {}ms with body : {}", request.getRequestURL(), endtime - startTime, mapper.writeValueAsString(result));
        }
    }

    /**
     * Log DELETE incoming request
     *
     * @param joinPoint join point used with aspect
     * @return result of method execution
     * @throws Throwable if an error occurs
     */
    @Around("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public Object logAroundDeleteMapping(ProceedingJoinPoint joinPoint) throws Throwable {

        // Log incoming request
        log.info("-> DELETE {}", request.getRequestURL());
        long startTime = System.currentTimeMillis();

        try {
            // Proceed method
            return joinPoint.proceed();

        } finally {
            long endtime = System.currentTimeMillis();

            // Log end incoming request
            log.info("<- DELETE {} in {}ms", request.getRequestURL(), endtime - startTime);
        }
    }

}
