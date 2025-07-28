package com.jvezolles.api.user;

import com.jvezolles.api.user.dto.UserDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * User controller
 * Rest controller for users, initialize and valid apis
 *
 * @author Vezolles
 */
@Validated
public interface UserController {

    /**
     * Api to get all user's details
     *
     * @param page page number search
     * @param size size number of element search
     * @return list of ser'information
     */
    @ResponseBody
    List<UserDTO> getUsers(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size);

    /**
     * Api to get user's details
     *
     * @param username the username to get details, must be size max = 100
     * @return user'information
     */
    @ResponseBody
    UserDTO getUser(@PathVariable @Valid @Size(max = 100, message = "{user.username.size}") String username);

    /**
     * Api to create user
     *
     * @param user the user's details to create, must be valid
     * @return user'information for user created
     */
    @ResponseBody
    UserDTO createUser(@RequestBody @Valid UserDTO user);

    /**
     * Api to delete user
     *
     * @param username the username to delete, must be size max = 100
     */
    void deleteUser(@PathVariable @Valid @Size(max = 100, message = "{user.username.size}") String username);

}
