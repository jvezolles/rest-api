package com.jvezolles.api.user;

import com.jvezolles.api.user.dto.UserDTO;
import com.jvezolles.api.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User's APIs", description = "APIs for reading, creating and deleting users")
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

    /**
     * Api to get all user's details
     *
     * @return all users informations and status HTTP 200
     */
    @Operation(summary = "Get all user's details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))})})
    @Override
    @GetMapping(value = "${jvezolles.api.user.get-all}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getUsers(Integer page, Integer size) {

        // Call service to get user
        List<User> usersFound = userService.getUsers(page, size);

        // Return all user as DTO
        return usersFound.stream().map(user -> userMapper.asUserDto(user)).toList();
    }


    /**
     * Api to get user's details
     *
     * @param username the username to get detail
     * @return user's information and status HTTP 200
     */
    @Operation(summary = "Get user details by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(description = "Error details", example = "User not found")))})
    @Override
    @GetMapping(value = "${jvezolles.api.user.get}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUser(String username) {

        // Call service to get user
        User userFound = userService.getUser(username);

        // Return user as DTO
        return userMapper.asUserDto(userFound);
    }

    /**
     * Api to create user
     *
     * @param user the user's details to create
     * @return user's information for user created and status HTTP 201
     */
    @Operation(summary = "Create new user with details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "500", description = "User cannot be created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(description = "Error details", example = "User cannot be created, user already exist")))})
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

    /**
     * Api to delete user, send status HTTP 204
     *
     * @param username the username to delete
     */
    @Operation(summary = "Delete existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted"),
            @ApiResponse(responseCode = "500", description = "User cannot be deleted",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(description = "Error details", example = "User cannot be deleted, user does not exist")))})
    @Override
    @DeleteMapping(value = "${jvezolles.api.user.delete}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(String username) {

        // Call service to delete user
        userService.deleteUser(username);
    }

}
