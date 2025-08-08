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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests E2E for user
 *
 * @author Vezolles
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserCreateE2ETest {

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

    private final LocalDate dateGuenievreDTO = LocalDate.now().minusYears(39);

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
     * Test creation new user
     *
     * @throws Exception if error occurs
     */
    @Test
    void testCreateUser() throws Exception {

        LocalDate dateKaradocDTO = LocalDate.now().minusYears(40);
        UserDTO user = new UserDTO("Karadoc", dateKaradocDTO, "France", "0644444444", "man", "karadoc@kaamelott.com");

        MvcResult result = mockMvc.perform(post("/user")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        UserDTO userDTOReceived = objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);

        assertNotNull(userDTOReceived);
        assertEquals("karadoc", userDTOReceived.username());
        assertEquals(dateKaradocDTO, userDTOReceived.birthdate());
        assertEquals("France", userDTOReceived.country());
        assertEquals("0644444444", userDTOReceived.phone());
        assertEquals("man", userDTOReceived.gender());
        assertEquals("karadoc@kaamelott.com", userDTOReceived.email());
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

        UserDTO user = new UserDTO("Guenièvre", dateGuenievreDTO, "France", "0622222222", "female", "reine@kaamelott.com");

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

        LocalDate dateYvainDTO = LocalDate.now().minusYears(17);
        UserDTO user = new UserDTO("Yvain", dateYvainDTO, "France", "0655555555", "man", "yvain@kaamelott.com");

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

        LocalDate datePercevalDTO = LocalDate.now().minusYears(50);
        UserDTO user = new UserDTO("Perceval", datePercevalDTO, "Pays de galles", "0666666666", "man", "perceval@kaamelott.com");

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

        LocalDate dateKaradocDTO = LocalDate.now().minusYears(40);
        UserDTO user = new UserDTO(null, dateKaradocDTO, "France", "0644444444", "man", "karadoc@kaamelott.com");

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

        LocalDate dateEliasDTO = LocalDate.now().minusYears(65);
        UserDTO user = new UserDTO("Elias de Kelliwic’h, dit « le Fourbe », grand enchanteur du Nord, meneur des loups de Calédonie, pourfendeur du dragon des neiges, concepteur de la potion de toute puissance, prophète des astres et grand ennemi de Merlin", dateEliasDTO, "France", "0677777777", "man", "elias@kaamelott.com");

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

        UserDTO user = new UserDTO("La Dame du Lac", null, "France", "0688888888", "female", "damedulac@kaamelott.com");

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

        String json = """
                {
                  "username": "La Dame du Lac",
                  "birthdate": "1900-01",
                  "country": "France",
                  "phone": "0688888888",
                  "gender": "female",
                  "email": "damedulac@kaamelott.com"
                }
                """;

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

        LocalDate dateBlaiseDTO = LocalDate.now().minusYears(55);
        UserDTO user = new UserDTO("Blaise", dateBlaiseDTO, null, "0699999999", "man", "pretre@kaamelott.com");

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

        LocalDate dateBlaiseDTO = LocalDate.now().minusYears(55);
        UserDTO user = new UserDTO("Blaise", dateBlaiseDTO, "Kaamelott la plus belle ville de toute la Bretagne et du monde, tout le reste n'est habité que par des païens", "0699999999", "man", "pretre@kaamelott.com");

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

        LocalDate dateCalogrenantDTO = LocalDate.now().minusYears(60);
        UserDTO user = new UserDTO("Calogrenant", dateCalogrenantDTO, "France", "necherchezpasàmejoindrejesuisoccupé", "man", "cestmoilevrairoi@kaamelott.com");

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

        LocalDate dateVenecDTO = LocalDate.now().minusYears(35);
        UserDTO user = new UserDTO("Venec", dateVenecDTO, "France", "0600000000", "HommeOuFemmeOuLesbienneOuGayOuBisexuelleOuTransOuQueerOuIntersexeOuAsexuellesOuTransidentitaireOuPlus", "venec@kaamelott.com");

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

        LocalDate dateGauvainDTO = LocalDate.now().minusYears(18);
        UserDTO user = new UserDTO("Gauvain", dateGauvainDTO, "France", "0612345678", "man", "gauvinlechevalieraupancréasneuveuduroiarthuretgrandamidyvainetchevalierdelatablerondedekaamelott@kaamelott.com");

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

        LocalDate dateGuethenocDTO = LocalDate.now().minusYears(18);
        UserDTO user = new UserDTO("Guethenoc", dateGuethenocDTO, "France", "0687654321", "man", "guethenoc.com");

        mockMvc.perform(post("/user")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
