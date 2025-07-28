package com.jvezolles.api.user;

import com.jvezolles.api.user.exception.UserCreateException;
import com.jvezolles.api.user.exception.UserDeleteException;
import com.jvezolles.api.user.exception.UserNotFoundException;
import com.jvezolles.api.user.model.User;
import com.jvezolles.api.util.Constants;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * User service implementation
 * Service implementation for users, make processing
 *
 * @author Vezolles
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    /**
     * Clock used for LocalDateTime
     */
    private Clock clock;

    /**
     * UserRepository used to persist user
     */
    private UserRepository userRepository;

    /**
     * Service to get all user's details
     *
     * @return list of user's information
     */
    @Override
    public List<User> getUsers(Integer page, Integer size) {

        // Creating page search
        if (size != null) {
            Pageable pageable = PageRequest.of(page != null ? page : 0, size);

            // Find all users for page
            return userRepository.findAll(pageable).toList();

        } else {
            // Find all users
            return userRepository.findAll();
        }
    }

    /**
     * Service to get user's details
     *
     * @param username the username to get detail
     * @return user'information
     * @throws UserNotFoundException if user doesn't exist
     */
    @Override
    public User getUser(String username) throws UserNotFoundException {

        // Find user if exists, else throw error user not found
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    /**
     * Service to create user
     *
     * @param user the user's details to create
     * @return user'information for user created
     * @throws UserCreateException if creation fails
     */
    @Override
    public User createUser(User user) throws UserCreateException {

        // Convert dates to local date time
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime birthdate = user.getBirthdate().toInstant().atZone(ZoneOffset.UTC).toLocalDateTime();

        // If user is not adult French, throw error user cannot be created, user must be adult French
        if (ChronoUnit.YEARS.between(birthdate, now) < 18 || !Constants.FRANCE.equalsIgnoreCase(user.getCountry())) {
            throw new UserCreateException("User cannot be created, user must be adult French");
        }

        // Find user by username
        Optional<User> userFound = userRepository.findByUsername(user.getUsername());

        // If user not exist
        if (userFound.isEmpty()) {

            try {
                // Persist new user
                return userRepository.save(user);

            } catch (IllegalArgumentException e) {
                throw new UserCreateException("User cannot be created");
            }

        } else {
            // Throw error user already exists
            throw new UserCreateException("User cannot be created, user already exists");
        }
    }

    /**
     * Service to delete user
     *
     * @param username the username to delete
     * @throws UserDeleteException if deletion fails
     */
    @Override
    public void deleteUser(String username) throws UserDeleteException {

        // Find user if exists, else throw error user does not exist
        User userFound = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserDeleteException("User cannot be deleted, user does not exist"));

        try {
            // Delete user
            userRepository.delete(userFound);

        } catch (IllegalArgumentException e) {
            throw new UserDeleteException("User cannot be deleted");
        }
    }

}
