package com.jvezolles.api.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * CLass DTO User for transfer
 *
 * @author Vezolles
 */
public record UserDTO(@NotBlank(message = "{user.username.mandatory}") @Size(max = 100, message = "{user.username.size}") String username,
                      @NotNull(message = "{user.birthday.mandatory}") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate birthdate,
                      @NotBlank(message = "{user.country.mandatory}") @Size(max = 100, message = "{user.country.size}") String country,
                      @Size(max = 15, message = "{user.phone.size}") String phone,
                      @Size(max = 100, message = "{user.gender.size}") String gender,
                      @Email(message = "{user.email.not-valid}") @Size(max = 100, message = "{user.email.size}") String email) {

}
