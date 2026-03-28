package br.social.impacthub.model.dto;

import java.util.UUID;

public record CreateOngInviteRequest(
        UUID userID,
        String role
) {
}
