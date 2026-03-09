package br.social.impacthub.model.dto;

import java.util.UUID;

public record UserCredentialsResponse(
        UUID userId,
        String username,
        String email
) {
}
