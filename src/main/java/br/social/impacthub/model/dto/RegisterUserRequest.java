package br.social.impacthub.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record RegisterUserRequest(
        @NotBlank String username,
        @Email String email,
        @NotBlank @Length(min = 8, max = 50) String password
)
{
}
