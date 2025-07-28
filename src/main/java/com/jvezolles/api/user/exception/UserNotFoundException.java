package com.jvezolles.api.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * Exception used if user not found
 *
 * @author Vezolles
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 5177479758077051072L;

    /**
     * UserNotFoundException default constructor
     *
     * @param message exception's message
     */
    public UserNotFoundException(String message) {
        super(message);
    }

}
