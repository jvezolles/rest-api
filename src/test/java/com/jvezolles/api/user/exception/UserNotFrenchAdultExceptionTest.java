package com.jvezolles.api.user.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for UserNotFrenchAdultException
 *
 * @author Vezolles
 */
@SpringBootTest
class UserNotFrenchAdultExceptionTest {

    @Test
    void testUserNotFrenchAdultException() {

        UserNotFrenchAdultException userNotFrenchAdultException = new UserNotFrenchAdultException("Error user update");

        assertNotNull(userNotFrenchAdultException);
        assertEquals("Error user update", userNotFrenchAdultException.getMessage());
    }

}
