package com.jvezolles.api.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

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
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 2934035488529223181L;

    /**
     * Attribute id with getter and setter
     * Auto-increment value
     */
    @Id
    @GeneratedValue
    @Column
    private Long id;

    /**
     * Attribute username with getter and setter
     */
    @Column
    private String username;

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

}
