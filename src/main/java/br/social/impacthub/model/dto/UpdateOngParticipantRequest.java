package br.social.impacthub.model.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateOngParticipantRequest(
        @NotBlank(message = "Role is required") String role
) {
}

