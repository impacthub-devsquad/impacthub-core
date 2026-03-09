package br.social.impacthub.model.dto.security;

import java.time.Instant;
import java.util.UUID;

public record Token(
        String rawToken,
        Instant expiration,
        String subject
) {
}
