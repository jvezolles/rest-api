package com.jvezolles.api.user.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for UserReplaceException
 *
 * @author Vezolles
 */
@SpringBootTest
class UserReplaceExceptionTest {

    @Test
    void testUserReplaceException() {

        UserReplaceException userReplaceException = new UserReplaceException("Error user not french adult");

        assertNotNull(userReplaceException);
        assertEquals("Error user not french adult", userReplaceException.getMessage());
    }

}
