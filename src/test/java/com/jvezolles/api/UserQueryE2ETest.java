package com.jvezolles.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvezolles.api.user.UserRepository;
import com.jvezolles.api.user.dto.UserDTO;
import com.jvezolles.api.user.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests E2E for user
 *
 * @author Vezolles
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserQueryE2ETest {

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

    private final LocalDate dateArthurDTO = LocalDate.now().minusYears(40);

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
     * Test get all user's details
     *
     * @throws Exception if error occurs
     */
    @Test
    void testGetAllUsers() throws Exception {

        MvcResult result = mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andReturn();

        List<UserDTO> usersDTOReceived = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertNotNull(usersDTOReceived);
        assertThat(usersDTOReceived.size(), is(3));
    }

    /**
     * Test get all user's details with page and size
     *
     * @throws Exception if error occurs
     */
    @ParameterizedTest
    @CsvSource({
            "0, 2, 2",
            "1, 2, 1"
    })
    void testGetAllUsersWithPageAndSize(Integer page, Integer size, int resultSize) throws Exception {

        MvcResult result = mockMvc.perform(get("/user?page=" + page + "&size=" + size))
                .andExpect(status().isOk())
                .andReturn();

        List<UserDTO> usersDTOReceived = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertNotNull(usersDTOReceived);
        assertThat(usersDTOReceived.size(), is(resultSize));
    }

    /**
     * Test get user's details
     *
     * @throws Exception if error occurs
     */
    @Test
    void testGetUser() throws Exception {

        MvcResult result = mockMvc.perform(get("/user/arthur"))
                .andExpect(status().isOk())
                .andReturn();

        UserDTO userDTOReceived = objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);

        assertNotNull(userDTOReceived);
        assertEquals("arthur", userDTOReceived.username());
        assertEquals(dateArthurDTO, userDTOReceived.birthdate());
        assertEquals("France", userDTOReceived.country());
        assertEquals("0611111111", userDTOReceived.phone());
        assertEquals("man", userDTOReceived.gender());
        assertEquals("roi@kaamelott.com", userDTOReceived.email());
    }

    /**
     * Test fail if user not exists
     *
     * @throws Exception if error occurs
     */
    @Test
    void testGetUserNotExist() throws Exception {

        mockMvc.perform(get("/user/lancelot"))
                .andExpect(status().isNotFound());
    }

}
