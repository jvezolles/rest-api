package com.jvezolles.api.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for UserDTO
 *
 * @author Vezolles
 */
@SpringBootTest
class UserDTOTest {

    @Test
    void testUserDTO() {

        LocalDate date = LocalDate.of(2002, 1, 8);

        UserDTO user1 = new UserDTO("test", date, "France", "0612345678", "man", "test@test.com");

        assertEquals("test", user1.username());
        assertEquals(date, user1.birthdate());
        assertEquals("France", user1.country());
        assertEquals("0612345678", user1.phone());
        assertEquals("man", user1.gender());
        assertEquals("test@test.com", user1.email());
    }

}
