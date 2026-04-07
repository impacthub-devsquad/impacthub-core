package br.social.impacthub.model.dto;

import br.social.impacthub.model.entity.UserProfile;

import java.time.Instant;
import java.util.UUID;

public interface EventSummary {
    UUID getId();
    String getTitle();
    String getDescription();
    UUID getOngId();
    Instant getCreatedAt();
    UserProfile getCreatedBy();
    Long getViewsCount();
    Long getLikesCount();
    Boolean getIsLiked();
}
