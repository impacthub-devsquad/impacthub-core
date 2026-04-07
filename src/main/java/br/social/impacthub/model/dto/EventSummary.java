package br.social.impacthub.model.dto;

import java.time.Instant;
import java.util.UUID;

public interface EventSummary {
    UUID getId();
    String getTitle();
    String getDescription();
    UUID getOngId();
    Instant getCreatedAt();
    UUID getCreatedBy();
    Long getLikesCount();
    Boolean getIsLiked();
}
