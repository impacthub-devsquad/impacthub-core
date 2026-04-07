package br.social.impacthub.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateEventRequest(
        @NotNull UUID ongId,
        @NotBlank String title,
        @NotBlank String description
) {
}
