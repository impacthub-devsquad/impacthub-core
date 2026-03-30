package br.social.impacthub.service.mapper;

import br.social.impacthub.model.dto.OngParticipantResponse;
import br.social.impacthub.model.entity.OngParticipant;
import org.springframework.stereotype.Component;

@Component
public class OngParticipantMapper {
    public OngParticipantResponse toResponse(OngParticipant ongParticipant) {
        return new OngParticipantResponse(
                ongParticipant.getUser().getUserId(),
                ongParticipant.getUser().getUsername(),
                ongParticipant.getUser().getEmail(),
                ongParticipant.getRole().getName()
        );
    }
}

