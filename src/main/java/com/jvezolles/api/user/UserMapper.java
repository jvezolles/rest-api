package com.jvezolles.api.user;

import com.jvezolles.api.user.dto.UserDTO;
import com.jvezolles.api.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * User mapper
 * Used for mapping of User
 *
 * @author Vezolles
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    /**
     * Convert a user as UserDto
     *
     * @param user the user to convert as UserDto
     * @return the UserDto
     */
    UserDTO asUserDto(User user);

    /**
     * Convert a userDto as User
     *
     * @param userDto the userDto to convert as User
     * @return the User
     */
    User asUser(UserDTO userDto);
}
