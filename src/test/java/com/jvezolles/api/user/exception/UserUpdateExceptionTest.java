package com.jvezolles.api.user.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for UserUpdateException
 *
 * @author Vezolles
 */
@SpringBootTest
class UserUpdateExceptionTest {

    @Test
    void testUserUpdateException() {

        UserUpdateException userUpdateException = new UserUpdateException("Error user update");

        assertNotNull(userUpdateException);
        assertEquals("Error user update", userUpdateException.getMessage());
    }

}
