package com.jvezolles.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvezolles.api.user.UserRepository;
import com.jvezolles.api.user.dto.UserDTO;
import com.jvezolles.api.user.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests E2E for user
 *
 * @author Vezolles
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserReplaceE2ETest {

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
     * Test replace user
     *
     * @throws Exception if error occurs
     */
    @Test
    void testReplaceUser() throws Exception {

        UserDTO user = new UserDTO("Arthur", dateArthurDTO, "France", "0611223344", "man", "leplusgrandroi@kaamelott.com");

        MvcResult result = mockMvc.perform(put("/user/arthur")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        UserDTO userDTOReceived = objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);

        assertNotNull(userDTOReceived);
        assertEquals("arthur", userDTOReceived.username());
        assertEquals(dateArthurDTO, userDTOReceived.birthdate());
        assertEquals("France", userDTOReceived.country());
        assertEquals("0611223344", userDTOReceived.phone());
        assertEquals("man", userDTOReceived.gender());
        assertEquals("leplusgrandroi@kaamelott.com", userDTOReceived.email());
    }

    /**
     * Test fail if replace user blank
     *
     * @throws Exception if error occurs
     */
    @Test
    void testReplaceUserBlank() throws Exception {

        mockMvc.perform(put("/user/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    /**
     * Test fail if replace user not exist
     *
     * @throws Exception if error occurs
     */
    @Test
    void testReplaceUserNotExist() throws Exception {

        LocalDate dateKaradocDTO = LocalDate.now().minusYears(40);
        UserDTO user = new UserDTO("Karadoc", dateKaradocDTO, "France", "0644444444", "man", "karadoc@kaamelott.com");

        mockMvc.perform(put("/user/karadoc")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    /**
     * Test fail if replace user not adult
     *
     * @throws Exception if error occurs
     */
    @Test
    void testReplaceUserNotAdult() throws Exception {

        LocalDate wrongDateArthurDTO = LocalDate.now().minusYears(17);
        UserDTO user = new UserDTO("Arthur", wrongDateArthurDTO, "France", "0611223344", "man", "leplusgrandroi@kaamelott.com");

        mockMvc.perform(put("/user/arthur")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    /**
     * Test fail if replace user not french
     *
     * @throws Exception if error occurs
     */
    @Test
    void testReplaceUserNotFrench() throws Exception {

        UserDTO user = new UserDTO("Arthur", dateArthurDTO, "Breton", "0611223344", "man", "leplusgrandroi@kaamelott.com");

        mockMvc.perform(put("/user/arthur")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    /**
     * Test fail if replace user with username missing
     *
     * @throws Exception if error occurs
     */
    @Test
    void testReplaceUserUsernameMissing() throws Exception {

        UserDTO user = new UserDTO(null, dateArthurDTO, "France", "0611223344", "man", "leplusgrandroi@kaamelott.com");

        mockMvc.perform(put("/user/arthur")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test fail if replace user with username too long
     *
     * @throws Exception if error occurs
     */
    @Test
    void testReplaceUserUsernameTooLong() throws Exception {

        UserDTO user = new UserDTO("arthurroidebretagnepourlinstantmaisbientotdumondeaussicarjesuislemeilleurroipossiblepourcemondeetlesautres", dateArthurDTO, "Breton", "0611223344", "man", "leplusgrandroi@kaamelott.com");

        mockMvc.perform(put("/user/arthur")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test fail if replace user with birthdate missing
     *
     * @throws Exception if error occurs
     */
    @Test
    void testReplaceUserBirthdateMissing() throws Exception {

        UserDTO user = new UserDTO("Arthur", null, "France", "0611223344", "man", "leplusgrandroi@kaamelott.com");

        mockMvc.perform(put("/user/arthur")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test fail if replace user with birthdate wrong format
     *
     * @throws Exception if error occurs
     */
    @Test
    void testReplaceUserBirthdateWrongFormat() throws Exception {

        String json = """
                {
                  "username": "Arthur",
                  "birthdate": "1980-01",
                  "country": "France",
                  "phone": "0611223344",
                  "gender": "man",
                  "email": "leplusgrandroi@kaamelott.com"
                }
                """;

        mockMvc.perform(put("/user/arthur")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test fail if replace user with country missing
     *
     * @throws Exception if error occurs
     */
    @Test
    void testReplaceUserCountryMissing() throws Exception {

        UserDTO user = new UserDTO("Arthur", dateArthurDTO, null, "0611223344", "man", "leplusgrandroi@kaamelott.com");

        mockMvc.perform(put("/user/arthur")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test fail if replace user with country too long
     *
     * @throws Exception if error occurs
     */
    @Test
    void testReplaceUserCountryTooLong() throws Exception {

        UserDTO user = new UserDTO("Arthur", dateArthurDTO, "Kaamelott la plus belle ville de toute la Bretagne et du monde, tout le reste n'est habité que par des païens", "0611223344", "man", "leplusgrandroi@kaamelott.com");

        mockMvc.perform(put("/user/arthur")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test fail if replace user with phone too long
     *
     * @throws Exception if error occurs
     */
    @Test
    void testReplaceUserPhoneTooLong() throws Exception {

        UserDTO user = new UserDTO("Arthur", dateArthurDTO, "France", "necherchezpasàmejoindrejesuisoccupé", "man", "leplusgrandroi@kaamelott.com");

        mockMvc.perform(put("/user/arthur")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test fail if replace user with gender too long
     *
     * @throws Exception if error occurs
     */
    @Test
    void testReplaceUserGenderTooLong() throws Exception {

        UserDTO user = new UserDTO("Arthur", dateArthurDTO, "France", "0611223344", "HommeOuFemmeOuLesbienneOuGayOuBisexuelleOuTransOuQueerOuIntersexeOuAsexuellesOuTransidentitaireOuPlus", "leplusgrandroi@kaamelott.com");

        mockMvc.perform(put("/user/arthur")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test fail if replace user with mail too long
     *
     * @throws Exception if error occurs
     */
    @Test
    void testReplaceUserMailTooLong() throws Exception {

        UserDTO user = new UserDTO("Arthur", dateArthurDTO, "France", "0611223344", "man", "leplusgrandroidekaamelottdelabretagnedumondeetdesautresaussigraceàmonépéelasainteexcalibur@kaamelott.com");

        mockMvc.perform(put("/user/arthur")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test fail if replace user with mail wrong format
     *
     * @throws Exception if error occurs
     */
    @Test
    void testReplaceUserMailWrongFormat() throws Exception {

        UserDTO user = new UserDTO("Arthur", dateArthurDTO, "France", "0611223344", "man", "kaamelott.com");

        mockMvc.perform(put("/user/arthur")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
