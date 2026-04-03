package br.social.impacthub.model.dto;

import br.social.impacthub.model.entity.OngCategory;

import java.time.Instant;
import java.util.UUID;

public interface OngSummary {
    UUID getId();
    UUID getUserOwnerId();
    String getName();
    String getTitle();
    String getDescription();
    OngCategory getCategory();
    Instant getCreatedAt();
    Long getParticipantsCount();
    Long getFollowersCount();
    Boolean getIsParticipant();
    Boolean getIsFollowing();
    Boolean getIsInvited();
}
