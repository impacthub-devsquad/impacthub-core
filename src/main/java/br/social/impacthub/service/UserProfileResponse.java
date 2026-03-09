package br.social.impacthub.service;

import java.util.UUID;

public record UserProfileResponse(
        UUID userId,
        String username
) {
}
