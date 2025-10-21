package com.jvezolles.api.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity User for persistence
 *
 * @author Vezolles
 */
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements UserDetails, Serializable {

    @Serial
    private static final long serialVersionUID = 2934035488529223181L;

    /**
     * Attribute id with getter and setter
     * Auto-increment value
     */
    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private Long id;

    /**
     * Attribute username with getter and setter
     */
    @Column(nullable = false)
    private String username;

    /**
     * Attribute password with getter and setter
     */
    @Column(nullable = false)
    private String password;

    /**
     * Attribute role with getter and setter
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Attribute birthdate with getter and setter
     * Format timestamp
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date birthdate;

    /**
     * Attribute country with getter and setter
     */
    @Column
    private String country;

    /**
     * Attribute phone with getter and setter
     */
    @Column
    private String phone;

    /**
     * Attribute gender with getter and setter
     */
    @Column
    private String gender;

    /**
     * Attribute email with getter and setter
     */
    @Column
    private String email;

    /**
     * Compute the authorities for the user
     */
    @Transient
    public Set<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        return authorities;
    }

}
