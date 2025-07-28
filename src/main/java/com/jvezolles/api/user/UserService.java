package com.jvezolles.api.user;

import java.util.List;

import com.jvezolles.api.user.exception.UserCreateException;
import com.jvezolles.api.user.exception.UserDeleteException;
import com.jvezolles.api.user.exception.UserNotFoundException;
import com.jvezolles.api.user.model.User;

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
	 * Service to delete user
	 * @param username the username to delete
	 * @throws UserDeleteException if deletion fails
	 */
    void deleteUser(String username) throws UserDeleteException;

}
