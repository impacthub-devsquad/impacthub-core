package br.social.impacthub.model.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateOngParticipantRoleRequest(
        @NotNull(message = "Role ID is required")
        Integer roleId
) {
}

