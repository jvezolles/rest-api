package com.jvezolles.api;

import com.jvezolles.api.user.UserRepository;
import com.jvezolles.api.user.dto.UserDTO;
import com.jvezolles.api.user.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests E2E for user
 *
 * @author Vezolles
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserE2ETest {

    @Mock
    private Clock clock;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private Date date = Date.from(LocalDate.of(2002, 1, 8).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
    private LocalDate dateDTO = LocalDate.of(2002, 1, 8);

    /**
     * Before each test, add user
     */
    @BeforeEach
    void setUp() {

        User user = new User();
        user.setUsername("test");
        user.setBirthdate(date);
        user.setCountry("France");
        user.setPhone("0612345678");
        user.setGender("man");
        user.setEmail("test@test.com");
        userRepository.save(user);

        User user2 = new User();
        user2.setUsername("test2");
        user2.setBirthdate(date);
        user2.setCountry("France");
        user2.setPhone("0612345678");
        user2.setGender("man");
        user2.setEmail("test@test.com");
        userRepository.save(user2);

        User user3 = new User();
        user3.setUsername("test3");
        user3.setBirthdate(date);
        user3.setCountry("France");
        user3.setPhone("0612345678");
        user3.setGender("man");
        user3.setEmail("test@test.com");
        userRepository.save(user3);

        Clock fixedClock = Clock.fixed(LocalDate.of(2020, 1, 8).atStartOfDay(ZoneOffset.UTC).toInstant(), ZoneOffset.UTC);
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();
    }

    /**
     * After each test, remove user
     */
    @AfterEach
    void tearDown() {
        Optional<User> user = userRepository.findByUsername("test");
        if (user.isPresent()) {
            userRepository.delete(user.get());
        }
        Optional<User> user2 = userRepository.findByUsername("test2");
        if (user2.isPresent()) {
            userRepository.delete(user2.get());
        }
        Optional<User> user3 = userRepository.findByUsername("test3");
        if (user3.isPresent()) {
            userRepository.delete(user3.get());
        }
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

        List<UserDTO> usersDTOReceived = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<UserDTO>>() {
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

        List<UserDTO> usersDTOReceived = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<UserDTO>>() {
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

        MvcResult result = mockMvc.perform(get("/user/test"))
                .andExpect(status().isOk())
                .andReturn();

        UserDTO userDTOReceived = objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);

        assertNotNull(userDTOReceived);
        assertEquals("test", userDTOReceived.getUsername());
        assertEquals(dateDTO, userDTOReceived.getBirthdate());
        assertEquals("France", userDTOReceived.getCountry());
        assertEquals("0612345678", userDTOReceived.getPhone());
        assertEquals("man", userDTOReceived.getGender());
        assertEquals("test@test.com", userDTOReceived.getEmail());
    }

    /**
     * Test fail if user not exists
     *
     * @throws Exception if error occurs
     */
    @Test
    void testGetUserNotExist() throws Exception {

        mockMvc.perform(get("/user/testnotexist"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test creation new user
     *
     * @throws Exception if error occurs
     */
    @Test
    void testCreateUser() throws Exception {

        UserDTO user = new UserDTO("testcreation", dateDTO, "France", "0612345678", "man", "test@test.com");

        MvcResult result = mockMvc.perform(post("/user")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        UserDTO userDTOReceived = objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);

        assertNotNull(userDTOReceived);
        assertEquals("testcreation", userDTOReceived.getUsername());
        assertEquals(dateDTO, userDTOReceived.getBirthdate());
        assertEquals("France", userDTOReceived.getCountry());
        assertEquals("0612345678", userDTOReceived.getPhone());
        assertEquals("man", userDTOReceived.getGender());
        assertEquals("test@test.com", userDTOReceived.getEmail());
    }

    /**
     * Test fail if creation user blank
     *
     * @throws Exception if error occurs
     */
    @Test
    void testCreateUserBlank() throws Exception {

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test fail if creation user already exist
     *
     * @throws Exception if error occurs
     */
    @Test
    void testCreateUserExist() throws Exception {

        UserDTO user = new UserDTO("test", dateDTO, "France", "0612345678", "man", "test@test.com");

        mockMvc.perform(post("/user")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    /**
     * Test fail if creation user not adult
     *
     * @throws Exception if error occurs
     */
    @Test
    void testCreateUserNotAdult() throws Exception {

        LocalDate dateNotAdult = LocalDate.of(2002, 1, 9);
        UserDTO user = new UserDTO("test", dateNotAdult, "France", "0612345678", "man", "test@test.com");

        mockMvc.perform(post("/user")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    /**
     * Test fail if creation user not french
     *
     * @throws Exception if error occurs
     */
    @Test
    void testCreateUserNotFrench() throws Exception {

        UserDTO user = new UserDTO("test", dateDTO, "Spain", "0612345678", "man", "test@test.com");

        mockMvc.perform(post("/user")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    /**
     * Test fail if creation user with username missing
     *
     * @throws Exception if error occurs
     */
    @Test
    void testCreateUserUsernameMissing() throws Exception {

        UserDTO user = new UserDTO(null, dateDTO, "France", "0612345678", "man", "test@test.com");

        mockMvc.perform(post("/user")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test fail if creation user with username too long
     *
     * @throws Exception if error occurs
     */
    @Test
    void testCreateUserUsernameTooLong() throws Exception {

        UserDTO user = new UserDTO("testtoooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooolong", dateDTO, "France", "0612345678", "man", "test@test.com");

        mockMvc.perform(post("/user")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test fail if creation user with birthdate missing
     *
     * @throws Exception if error occurs
     */
    @Test
    void testCreateUserBirthdateMissing() throws Exception {

        UserDTO user = new UserDTO("testcreation", null, "France", "0612345678", "man", "test@test.com");

        mockMvc.perform(post("/user")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test fail if creation user with birthdate wrong format
     *
     * @throws Exception if error occurs
     */
    @Test
    void testCreateUserBirthdateWrongFormat() throws Exception {

        String json = "{\"username\":\"testcreation\",\"birthdate\":\"2002-01\",\"country\":\"France\",\"phone\":\"0612345678\",\"gender\":\"man\",\"email\":\"test@test.com\"}";

        mockMvc.perform(post("/user")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test fail if creation user with country missing
     *
     * @throws Exception if error occurs
     */
    @Test
    void testCreateUserCountryMissing() throws Exception {

        UserDTO user = new UserDTO("testcreation", dateDTO, null, "0612345678", "man", "test@test.com");

        mockMvc.perform(post("/user")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test fail if creation user with country too long
     *
     * @throws Exception if error occurs
     */
    @Test
    void testCreateUserCountryTooLong() throws Exception {

        UserDTO user = new UserDTO("testcreation", dateDTO, "Francetoooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooolong", "0612345678", "man", "test@test.com");

        mockMvc.perform(post("/user")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test fail if creation user with phone too long
     *
     * @throws Exception if error occurs
     */
    @Test
    void testCreateUserPhoneTooLong() throws Exception {

        UserDTO user = new UserDTO("testcreation", dateDTO, "France", "0612345678toooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooolong", "man", "test@test.com");

        mockMvc.perform(post("/user")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test fail if creation user with gender too long
     *
     * @throws Exception if error occurs
     */
    @Test
    void testCreateUserGenderTooLong() throws Exception {

        UserDTO user = new UserDTO("testcreation", dateDTO, "France", "0612345678", "gendertoooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooolong", "test@test.com");

        mockMvc.perform(post("/user")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test fail if creation user with mail too long
     *
     * @throws Exception if error occurs
     */
    @Test
    void testCreateUserMailTooLong() throws Exception {

        UserDTO user = new UserDTO("testcreation", dateDTO, "France", "0612345678", "man", "testtoooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooolong@test.com");

        mockMvc.perform(post("/user")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test fail if creation user with mail wrong format
     *
     * @throws Exception if error occurs
     */
    @Test
    void testCreateUserMailWrongFormat() throws Exception {

        UserDTO user = new UserDTO("testcreation", dateDTO, "France", "0612345678", "man", "test.com");

        mockMvc.perform(post("/user")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test delete user
     *
     * @throws Exception if error occurs
     */
    @Test
    void testDeleteUser() throws Exception {

        mockMvc.perform(delete("/user/test"))
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

        mockMvc.perform(delete("/user/testnotexist"))
                .andExpect(status().isInternalServerError());
    }

}
