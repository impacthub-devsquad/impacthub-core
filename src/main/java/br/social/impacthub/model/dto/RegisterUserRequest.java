package br.social.impacthub.model.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record RegisterUserRequest(
        String username,
        String email,
        @NotBlank @Length(min = 8, max = 50) String password
)
{
}
