package br.social.impacthub.model.dto;

import java.util.UUID;

public record OngResponse(
        UUID ongId,
        //UUID userOwnerId,
        String name,
        String title,
        String description
        //Long participantsCount,
        //Long followersCount,
        //boolean isParticipant,
        //boolean isFollowing,
        //boolean isInvited
) {
}
