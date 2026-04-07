package br.social.impacthub.model.dto;

import java.util.UUID;

public record UserProfileResponse(
        UUID userId,
        String username,
        String name,
        String email
) {
}
