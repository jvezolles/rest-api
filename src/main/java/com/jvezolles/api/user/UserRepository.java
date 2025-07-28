package com.jvezolles.api.user;

import com.jvezolles.api.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User repository
 * Used for persistance of User entity
 *
 * @author Vezolles
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by username in database
     *
     * @param username the username to find
     * @return an optional user if found
     */
    Optional<User> findByUsername(String username);

}
