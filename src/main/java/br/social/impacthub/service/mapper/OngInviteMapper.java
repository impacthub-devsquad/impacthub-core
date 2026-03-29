package br.social.impacthub.service.mapper;

import br.social.impacthub.model.dto.OngInviteResponse;
import br.social.impacthub.model.entity.OngInvite;
import org.springframework.stereotype.Component;

@Component
public class OngInviteMapper {
    public OngInviteResponse toResponse(OngInvite ongInvite){
        return new OngInviteResponse(
                ongInvite.getOng().getId(),
                ongInvite.getUser().getUserId(),
                ongInvite.getRole().getName(),
                ongInvite.getCreatedAt()
        );
    }
}
