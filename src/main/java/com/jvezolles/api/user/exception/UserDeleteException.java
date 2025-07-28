package com.jvezolles.api.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * Exception used if user deletion fails
 *
 * @author Vezolles
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UserDeleteException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 2292491813517932883L;

    /**
     * UserDeleteException default constructor
     *
     * @param message exception's message
     */
    public UserDeleteException(String message) {
        super(message);
    }

}
