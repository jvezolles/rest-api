package com.jvezolles.api.user.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for UserCreateException
 *
 * @author Vezolles
 */
@SpringBootTest
class UserCreateExceptionTest {

    @Test
    void testUserCreateException() {

        UserCreateException userCreateException = new UserCreateException("Error user creation");

        assertNotNull(userCreateException);
        assertEquals("Error user creation", userCreateException.getMessage());
    }

}
