package br.social.impacthub.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public record UpdateEventRequest(
        Optional<String> title,
        Optional<String> description
) {
}
