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

    private Object logAround(ProceedingJoinPoint joinPoint, String method, boolean isWithBody) throws Throwable {

        Object result = null;
        String body = request.getReader().lines().collect(Collectors.joining());

        // Log incoming request
        if (isWithBody) {
            log.info("-> {} {} with body : {}", method, request.getRequestURL(), body);
        } else {
            log.info("-> {} {}", method, request.getRequestURL());
        }

        long startTime = System.currentTimeMillis();

        try {
            // Proceed method
            result = joinPoint.proceed();
            return result;

        } finally {
            long endtime = System.currentTimeMillis();

            // Log end incoming request
            log.info("<- {} {} in {}ms with body : {}", method, request.getRequestURL(), endtime - startTime, mapper.writeValueAsString(result));
        }
    }

    /**
     * Log GET incoming request
     *
     * @param joinPoint join point used with aspect
     * @return result of method execution
     * @throws Throwable if an error occurs
     */
    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public Object logAroundGetMapping(ProceedingJoinPoint joinPoint) throws Throwable {
        return logAround(joinPoint, "GET", Boolean.FALSE);
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
        return logAround(joinPoint, "POST", Boolean.TRUE);
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
        return logAround(joinPoint, "PATCH", Boolean.TRUE);
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
        return logAround(joinPoint, "PUT", Boolean.TRUE);
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
        return logAround(joinPoint, "DELETE", Boolean.FALSE);
    }

}
