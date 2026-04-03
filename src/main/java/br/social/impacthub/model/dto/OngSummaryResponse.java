package br.social.impacthub.model.dto;

import java.time.Instant;
import java.util.UUID;

public record OngSummaryResponse(
        UUID ongId,
        UUID userOwnerId,
        String name,
        String title,
        String description,
        String category,
        Instant createdAt,
        Long participantsCount,
        Long followersCount,
        Boolean isParticipant,
        Boolean isFollowing,
        Boolean isInvited
) {
}
