package br.social.impacthub.model.dto.security;

import br.social.impacthub.model.entity.RefreshToken;

import java.time.Instant;
import java.util.UUID;

public record DecodedRefreshToken(
        UUID tokenId,
        UUID userId,
        Instant expiration
) {}
