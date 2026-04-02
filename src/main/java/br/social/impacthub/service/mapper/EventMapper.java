package br.social.impacthub.service.mapper;

import br.social.impacthub.model.dto.EventResponse;
import br.social.impacthub.model.entity.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {
    public EventResponse toResponse(Event event) {
        return new EventResponse(
                event.getEventId(),
                event.getTitle(),
                event.getDescription()
        );
    }

}
