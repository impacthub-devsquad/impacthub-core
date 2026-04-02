package br.social.impacthub.service.mapper;

import br.social.impacthub.model.dto.CreateOngRequest;
import br.social.impacthub.model.dto.OngResponse;
import br.social.impacthub.model.dto.OngSummary;
import br.social.impacthub.model.entity.Ong;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class OngMapper {
    public OngResponse toResponse(Ong ong) {
        return new OngResponse(
            ong.getOngId(),
//          ong.getUserOwner().getUserId(),
            ong.getName(),
            ong.getTitle(),
            ong.getDescription()
        );
    }

    //public OngResponse toResponse(OngSummary ongSummary) {
    //    return new OngResponse(
    //            ongSummary.getId(),
    //            ongSummary.getUserOwnerId(),
    //            ongSummary.getName(),
    //            ongSummary.getDescription(),
    //            ongSummary.getParticipantsCount(),
    //            ongSummary.getFollowersCount(),
    //            ongSummary.isParticipant(),
    //            ongSummary.isFollowing(),
    //            ongSummary.isInvited()
    //    );
    //}

    public Ong toEntity(@Valid CreateOngRequest request) {
        return new Ong(
                null,
                request.name(),
                request.title(),
                request.description(),
                null,
                null
        );
    }
}
