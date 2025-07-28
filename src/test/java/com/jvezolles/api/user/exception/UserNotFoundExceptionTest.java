package com.jvezolles.api.user.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for UserNotFoundException
 *
 * @author Vezolles
 */
@SpringBootTest
class UserNotFoundExceptionTest {

    @Test
    void testUserNotFoundException() {

        UserNotFoundException userNotFoundException = new UserNotFoundException("Error user not found");

        assertNotNull(userNotFoundException);
        assertEquals("Error user not found", userNotFoundException.getMessage());
    }

}
