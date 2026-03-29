package br.social.impacthub.model.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateOngParticipantInviteRequest(
        @NotNull(message = "User ID is required")
        UUID userId,
        @NotNull(message = "Role ID is required")
        Integer roleId
) {
}

