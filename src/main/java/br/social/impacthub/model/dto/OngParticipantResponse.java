package br.social.impacthub.model.dto;

import java.util.UUID;

public record OngParticipantResponse(
        UUID userId,
        String userName,
        String userEmail,
        String role
) {
}

