package com.jvezolles.api.user;

import com.jvezolles.api.user.exception.*;
import com.jvezolles.api.user.model.User;

import java.util.List;

/**
 * User service
 * Service for users, make processing
 * @author Vezolles
 */
public interface UserService {
	
	/**
	 * Service to get all user's details
	 * @param page page number search
	 * @param size size number of element search
	 * @return list of user's information
	 */
	List<User> getUsers(Integer page, Integer size);
	
	/**
	 * Service to get user's details
	 * @param username the username to get details
	 * @return user'information
	 * @throws UserNotFoundException if user doesn't exist
	 */
	User getUser(String username) throws UserNotFoundException;
    
	/**
	 * Service to create user
	 * @param user the user's details to create
	 * @return user'information for user created
	 * @throws UserCreateException if creation fails
	 */
	User createUser(User user) throws UserCreateException;

    /**
     * Service to update user
     * @param user the user's details to update
     * @return user'information for user updated
     * @throws UserUpdateException if creation fails
     */
    User updateUser(User user) throws UserUpdateException;

    /**
     * Service to replace user
     * @param username the username to replace
     * @param user the user's details to replace
     * @return user'information for user replaced
     * @throws UserReplaceException if creation fails
     */
    User replaceUser(String username, User user) throws UserReplaceException;
    
	/**
	 * Service to delete user
	 * @param username the username to delete
	 * @throws UserDeleteException if deletion fails
	 */
    void deleteUser(String username) throws UserDeleteException;

}
