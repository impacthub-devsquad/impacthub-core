package br.social.impacthub.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

public record UpdateUserRequest (
        Optional<@NotBlank String> username,
        Optional<@NotBlank String> description
){
}
