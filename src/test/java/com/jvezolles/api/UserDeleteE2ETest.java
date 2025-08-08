package com.jvezolles.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvezolles.api.user.UserRepository;
import com.jvezolles.api.user.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests E2E for user
 *
 * @author Vezolles
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserDeleteE2ETest {

    @Mock
    private Clock clock;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private final Date dateArthur = Date.from(LocalDate.now().minusYears(40).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
    private final Date dateGuenievre = Date.from(LocalDate.now().minusYears(39).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
    private final Date dateMerlin = Date.from(LocalDate.now().minusYears(70).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());

    /**
     * Before each test, add user
     */
    @BeforeEach
    void setUp() {

        User user = new User();
        user.setUsername("arthur");
        user.setBirthdate(dateArthur);
        user.setCountry("France");
        user.setPhone("0611111111");
        user.setGender("man");
        user.setEmail("roi@kaamelott.com");
        userRepository.save(user);

        User user2 = new User();
        user2.setUsername("guenièvre");
        user2.setBirthdate(dateGuenievre);
        user2.setCountry("France");
        user2.setPhone("0622222222");
        user2.setGender("female");
        user2.setEmail("reine@kaamelott.com");
        userRepository.save(user2);

        User user3 = new User();
        user3.setUsername("merlin");
        user3.setBirthdate(dateMerlin);
        user3.setCountry("France");
        user3.setPhone("0633333333");
        user3.setGender("man");
        user3.setEmail("merlin@kaamelott.com");
        userRepository.save(user3);

        Clock fixedClock = Clock.fixed(LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant(), ZoneOffset.UTC);
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();
    }

    /**
     * After each test, remove user
     */
    @AfterEach
    void tearDown() {
        userRepository.findAll().forEach(value -> userRepository.delete(value));
    }

    /**
     * Test delete user
     *
     * @throws Exception if error occurs
     */
    @Test
    void testDeleteUser() throws Exception {

        mockMvc.perform(delete("/user/arthur"))
                .andExpect(status().isNoContent());
    }

    /**
     * Test fail if deletion user blank
     *
     * @throws Exception if error occurs
     */
    @Test
    void testDeleteUserBlank() throws Exception {

        mockMvc.perform(delete("/user/"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test fail if deletion user not exist
     *
     * @throws Exception if error occurs
     */
    @Test
    void testDeleteUserNotExist() throws Exception {

        mockMvc.perform(delete("/user/loth"))
                .andExpect(status().isInternalServerError());
    }

}
