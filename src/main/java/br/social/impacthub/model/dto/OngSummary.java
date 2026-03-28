package br.social.impacthub.model.dto;

import java.util.UUID;

public interface OngSummary {
    UUID getId();
    UUID getUserOwnerId();
    String getName();
    String getDescription();
    Long getParticipantsCount();
    Long getFollowersCount();
    boolean isParticipant();
    boolean isFollowing();
    boolean isInvited();
}
