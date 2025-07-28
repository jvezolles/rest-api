package com.jvezolles.api.user.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for UserDeleteException
 *
 * @author Vezolles
 */
@SpringBootTest
class UserDeleteExceptionTest {

    @Test
    void testUserDeleteException() {

        UserDeleteException userDeleteException = new UserDeleteException("Error user deletion");

        assertNotNull(userDeleteException);
        assertEquals("Error user deletion", userDeleteException.getMessage());
    }

}
