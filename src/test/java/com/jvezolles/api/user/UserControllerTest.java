package com.jvezolles.api.user;

import com.jvezolles.api.user.dto.UserDTO;
import com.jvezolles.api.user.model.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for UserController
 *
 * @author Vezolles
 */
@SpringBootTest
class UserControllerTest {

    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController = new UserControllerImpl(userMapper, userService);

    private final Date date = Date.from(LocalDate.of(2002, 1, 8).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
    private final User userWithoutId = new User(null, "test", date, "France", "0612345678", "man", "test@test.com");
    private final User user = new User(1L, "test", date, "France", "0612345678", "man", "test@test.com");
    private final User userUpdated = new User(1L, "test2", date, "France", "0612345678", "female", "test@test.com");
    private final User user2 = new User(2L, "test2", date, "France", "0612345678", "man", "test@test.com");
    private final User user3 = new User(3L, "test3", date, "France", "0612345678", "man", "test@test.com");
    private final LocalDate dateDTO = LocalDate.of(2002, 1, 8);
    private final UserDTO userDTO = new UserDTO("test", dateDTO, "France", "0612345678", "man", "test@test.com");
    private final UserDTO userDTO2 = new UserDTO("test2", dateDTO, "France", "0612345678", "man", "test@test.com");
    private final UserDTO userDTO3 = new UserDTO("test3", dateDTO, "France", "0612345678", "man", "test@test.com");

    @Test
    void testGetAllUser() {

        when(userService.getUsers(1, 3)).thenReturn(List.of(user, user2, user3));

        List<UserDTO> result = userController.getUsers(1, 3);

        verify(userService).getUsers(1, 3);
        assertNotNull(result);
        assertThat(result.size(), is(3));
        assertThat(result, contains(userDTO, userDTO2, userDTO3));
    }

    @Test
    void testGetUser() {

        when(userService.loadUserByUsername("test")).thenReturn(user);

        UserDTO result = userController.getUser("test");

        verify(userService).loadUserByUsername("test");
        assertNotNull(result);
        assertEquals("test", result.username());
        assertEquals(dateDTO, result.birthdate());
        assertEquals("France", result.country());
        assertEquals("0612345678", result.phone());
        assertEquals("man", result.gender());
        assertEquals("test@test.com", result.email());
    }

    @Test
    void testCreateUser() {

        when(userService.createUser(userWithoutId)).thenReturn(user);

        userController.createUser(userDTO);

        verify(userService).createUser(userWithoutId);
    }

    @Test
    void testUpdateUser() {

        when(userService.updateUser(userWithoutId)).thenReturn(userUpdated);

        userController.updateUser(userDTO);

        verify(userService).updateUser(userWithoutId);
    }

    @Test
    void testReplaceUser() {

        when(userService.replaceUser("test", userWithoutId)).thenReturn(userUpdated);

        userController.replaceUser("test", userDTO);

        verify(userService).replaceUser("test", userWithoutId);
    }

    @Test
    void testDeleteUser() {

        userController.deleteUser("test");

        verify(userService).deleteUser("test");
    }

}
