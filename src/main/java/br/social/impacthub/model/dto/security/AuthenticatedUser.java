package br.social.impacthub.model.dto.security;

import java.util.UUID;

public record AuthenticatedUser(
        UUID userId,
        String username,
        String email
) {
}
