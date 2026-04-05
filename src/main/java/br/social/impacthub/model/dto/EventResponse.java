package br.social.impacthub.model.dto;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record EventResponse(
        UUID id,
        String title,
        String description,
        UUID ongId,
        Instant createdAt,
        UUID createdBy,
        Long likesCount
) {
}
