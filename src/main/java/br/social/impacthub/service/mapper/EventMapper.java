package br.social.impacthub.service.mapper;

import br.social.impacthub.model.dto.EventResponse;
import br.social.impacthub.model.dto.EventSummary;
import br.social.impacthub.model.entity.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {
    public EventResponse toResponse(EventSummary eventSummary) {
        return new EventResponse(
                eventSummary.getId(),
                eventSummary.getTitle(),
                eventSummary.getDescription(),
                eventSummary.getOngId(),
                eventSummary.getCreatedAt(),
                eventSummary.getCreatedBy(),
                eventSummary.getLikesCount(),
                eventSummary.getIsLiked()
        );
    }

}
