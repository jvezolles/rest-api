package com.jvezolles.api.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * Exception used if user update fails
 *
 * @author Vezolles
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UserUpdateException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4963566993769703058L;

    /**
     * UserUpdateException default constructor
     *
     * @param message exception's message
     */
    public UserUpdateException(String message) {
        super(message);
    }

}
