package br.social.impacthub.service.mapper;

import br.social.impacthub.model.dto.OngResponse;
import br.social.impacthub.model.dto.OngSummaryResponse;
import br.social.impacthub.model.dto.OngSummary;
import br.social.impacthub.model.entity.Ong;
import org.springframework.stereotype.Component;

@Component
public class OngMapper {
    public OngSummaryResponse toResponse(OngSummary ongSummary) {
        return new OngSummaryResponse(
                ongSummary.getId(),
                ongSummary.getUserOwnerId(),
                ongSummary.getName(),
                ongSummary.getTitle(),
                ongSummary.getDescription(),
                ongSummary.getCategory().getName(),
                ongSummary.getCreatedAt(),
                ongSummary.getParticipantsCount(),
                ongSummary.getFollowersCount(),
                ongSummary.getIsParticipant(),
                ongSummary.getIsFollowing(),
                ongSummary.getIsInvited()
        );
    }

    public OngResponse toResponse(Ong ong){
        return new OngResponse(
                ong.getId(),
                ong.getUserOwner().getUserId(),
                ong.getName(),
                ong.getTitle(),
                ong.getDescription(),
                ong.getCategory().getName(),
                ong.getCreatedAt()
        );
    }
}
