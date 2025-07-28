package com.jvezolles.api.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

/**
 * CLass DTO User for transfer
 *
 * @author Vezolles
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class UserDTO {

    /**
     * Attribute username with getter and setter
     * Username must be not blank and max size = 100
     */
    @NotBlank(message = "{user.username.mandatory}")
    @Size(max = 100, message = "{user.username.size}")
    private String username;

    /**
     * Attribute birthdate with getter and setter
     * Birthdate must be not null and format = yyyy-MM-dd
     */
    @NotNull(message = "{user.birthday.mandatory}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    /**
     * Attribute country with getter and setter
     * Country must be not blank and max size = 100
     */
    @NotBlank(message = "{user.country.mandatory}")
    @Size(max = 100, message = "{user.country.size}")
    private String country;

    /**
     * Attribute phone with getter and setter
     * Phone must be max size = 15
     */
    @Size(max = 15, message = "{user.phone.size}")
    private String phone;

    /**
     * Attribute gender with getter and setter
     * Gender must be max size = 100
     */
    @Size(max = 100, message = "{user.gender.size}")
    private String gender;

    /**
     * Attribute email with getter and setter
     * Email must be valid format and max size = 100
     */
    @Email(message = "{user.email.not-valid}")
    @Size(max = 100, message = "{user.email.size}")
    private String email;

}
