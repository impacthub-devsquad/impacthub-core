package br.social.impacthub.model.dto;

import br.social.impacthub.model.entity.UserProfile;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record EventResponse(
        UUID id,
        String title,
        String description,
        UUID ongId,
        Instant createdAt,
        UserProfileResponse createdBy,
        Long viewsCount,
        Long likesCount,
        Boolean isLiked
) {
}
