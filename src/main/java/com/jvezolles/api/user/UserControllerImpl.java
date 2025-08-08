package com.jvezolles.api.user;

import com.jvezolles.api.user.dto.UserDTO;
import com.jvezolles.api.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User controller implementation
 * Rest controller implementation for users, initialize and valid apis
 *
 * @author Vezolles
 */
@RestController
@AllArgsConstructor
public class UserControllerImpl implements UserController {

    /**
     * UserMapper used to map different model
     */
    private UserMapper userMapper;

    /**
     * UserService used to make user's processing
     */
    private UserService userService;

    @Override
    @GetMapping(value = "${jvezolles.api.user.get-all}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getUsers(Integer page, Integer size) {

        // Call service to get user
        List<User> usersFound = userService.getUsers(page, size);

        // Return all user as DTO
        return usersFound.stream().map(user -> userMapper.asUserDto(user)).toList();
    }

    @Override
    @GetMapping(value = "${jvezolles.api.user.get}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUser(String username) {

        // Call service to get user
        User userFound = userService.getUser(username);

        // Return user as DTO
        return userMapper.asUserDto(userFound);
    }

    @Override
    @PostMapping(value = "${jvezolles.api.user.create}")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(UserDTO user) {

        // Transform DTO to User
        User userReceived = userMapper.asUser(user);

        // Call service to create user
        User userCreated = userService.createUser(userReceived);

        // Return user as DTO
        return userMapper.asUserDto(userCreated);
    }

    @Override
    @PatchMapping(value = "${jvezolles.api.user.update}")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO updateUser(UserDTO user) {

        // Transform DTO to User
        User userReceived = userMapper.asUser(user);

        // Call service to update user
        User userUpdated = userService.updateUser(userReceived);

        // Return user as DTO
        return userMapper.asUserDto(userUpdated);
    }

    @Override
    @PutMapping(value = "${jvezolles.api.user.replace}")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO replaceUser(String username, UserDTO user) {

        // Transform DTO to User
        User userReceived = userMapper.asUser(user);

        // Call service to replace user
        User userReplaced = userService.replaceUser(username, userReceived);

        // Return user as DTO
        return userMapper.asUserDto(userReplaced);
    }

    @Override
    @DeleteMapping(value = "${jvezolles.api.user.delete}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(String username) {

        // Call service to delete user
        userService.deleteUser(username);
    }

}
