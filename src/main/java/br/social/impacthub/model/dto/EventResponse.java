package br.social.impacthub.model.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EventResponse(UUID eventId, @NotNull String title, @NotNull String description) {
}
