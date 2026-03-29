package br.social.impacthub.model.dto;

import java.time.Instant;
import java.util.UUID;

public record OngInviteResponse (
        UUID ongId,
        UUID userId,
        String role,
        Instant createdAt
){
}
