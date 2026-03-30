package br.social.impacthub.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record RegisterUserRequest(
        @NotBlank String username,
        @Email(message = "Must be a well-formed email address") String email,
        @NotBlank @Length(min = 8, max = 50, message = "Length must be between 8 and 50") String password
)
{
}
