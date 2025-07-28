package com.jvezolles.api.user.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for UserTest
 *
 * @author Vezolles
 */
@SpringBootTest
class UserTest {

    @Test
    void testUser() {

        Date date = Date.from(LocalDate.of(2002, 1, 8).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());

        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("test");
        user1.setBirthdate(date);
        user1.setCountry("France");
        user1.setPhone("0612345678");
        user1.setGender("man");
        user1.setEmail("test@test.com");

        assertEquals(1L, user1.getId().longValue());
        assertEquals("test", user1.getUsername());
        assertEquals(date, user1.getBirthdate());
        assertEquals("France", user1.getCountry());
        assertEquals("0612345678", user1.getPhone());
        assertEquals("man", user1.getGender());
        assertEquals("test@test.com", user1.getEmail());

        User user2 = new User(1L, "test", date, "France", "0612345678", "man", "test@test.com");

        assertEquals(1L, user2.getId().longValue());
        assertEquals("test", user2.getUsername());
        assertEquals(date, user2.getBirthdate());
        assertEquals("France", user2.getCountry());
        assertEquals("0612345678", user2.getPhone());
        assertEquals("man", user2.getGender());
        assertEquals("test@test.com", user2.getEmail());
    }

}
