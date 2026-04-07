package br.social.impacthub.service;

import br.social.impacthub.exception.*;
import br.social.impacthub.infrastructure.persistence.*;
import br.social.impacthub.model.dto.*;
import br.social.impacthub.model.entity.*;
import br.social.impacthub.service.mapper.EventMapper;
import br.social.impacthub.service.mapper.UserProfileMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final OngRepository ongRepository;
    private final OngParticipantRepository ongParticipantRepository;
    private final UserProfileRepository userProfileRepository;
    private final EventLikeRepository eventLikeRepository;
    private final UserProfileMapper userProfileMapper;

    public EventService(EventRepository eventRepository, EventMapper eventMapper, OngRepository ongRepository, OngParticipantRepository ongParticipantRepository, UserProfileRepository userProfileRepository, EventLikeRepository eventLikeRepository, UserProfileMapper userProfileMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.ongRepository = ongRepository;
        this.ongParticipantRepository = ongParticipantRepository;
        this.userProfileRepository = userProfileRepository;
        this.eventLikeRepository = eventLikeRepository;
        this.userProfileMapper = userProfileMapper;
    }

    public EventResponse getByEventId(UUID eventId, UUID authenticatedUserId) {
        return eventMapper.toResponse(
                eventRepository.getEventSummaryById(eventId, authenticatedUserId)
                    .orElseThrow(() -> new EventNotFoundException("Event not found with id: "+eventId))
        );
    }

    public PagedResponse<EventResponse> getAll(UUID authenticatedUserId, Pageable pageable) {
        Page<EventSummary> page = eventRepository.getAllEventSummary(authenticatedUserId, pageable);

        return PagedResponse.<EventResponse>builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .isLast(page.isLast())
                .content(page.getContent().stream()
                        .map(eventMapper::toResponse)
                        .toList()
                )
                .build();
    }

    public PagedResponse<EventResponse> getAllByOngId(UUID ongId, UUID authenticatedUserId, Pageable pageable) {
        Page<EventSummary> page = eventRepository.getAllEventSummaryByOngId(ongId, authenticatedUserId, pageable);

        return PagedResponse.<EventResponse>builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .isLast(page.isLast())
                .content(page.getContent().stream()
                        .map(eventMapper::toResponse)
                        .toList()
                )
                .build();
    }

    public EventResponse create(@Valid CreateEventRequest request, UUID authenticatedUserId) {
        Ong ong = ongRepository.findById(request.ongId())
                .orElseThrow(() -> new OngNotFoundException("Ong not found with id: "+request.ongId()));

        UserProfile userProfile = userProfileRepository.getReferenceById(authenticatedUserId);

        OngParticipant ongParticipant = ongParticipantRepository.findById(
                new OngParticipantId(ong, userProfile)
        ).orElseThrow(() -> new ForbiddenOperationException("User is not participant of this ONG"));

        if (!canOngParticipantManageEvents(ongParticipant))
            throw new ForbiddenOperationException("User can't manage events");

        Event newEvent = new Event();
        newEvent.setOng(ong);
        newEvent.setTitle(request.title());
        newEvent.setDescription(request.description());
        newEvent.setViewsCount(0L);
        newEvent.setCreatedAt(Instant.now());
        newEvent.setCreatedBy(userProfile);

        UUID newEventId = eventRepository.save(newEvent).getId();

        return getByEventId(newEventId, authenticatedUserId);
    }

    private boolean canOngParticipantManageEvents(@Valid OngParticipant ongParticipant) {
        List<OngParticipantRole.Value> allowedRoles = List.of(
                OngParticipantRole.Value.OWNER,
                OngParticipantRole.Value.ADM
        );

        OngParticipantRole.Value participantRole = OngParticipantRole.Value.fromName(
                ongParticipant.getRole().getName()
        ).orElseThrow(() -> new InvalidOngParticipantRoleException("Invalid participant role"));

        return allowedRoles.contains(participantRole);
    }


    public void deleteByEventId(UUID eventId, UUID authenticatedUserId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: "+eventId));

        UserProfile authenticatedUserProfile = userProfileRepository.getReferenceById(authenticatedUserId);

        Ong ong = ongRepository.getReferenceById(event.getOng().getId());

        OngParticipant authenticatedOngParticipant = ongParticipantRepository.findById(
                new OngParticipantId(
                        ong, authenticatedUserProfile
                )
        ).orElseThrow(() -> new ForbiddenOperationException("User is not participant of this ONG"));

        if (!canOngParticipantManageEvents(authenticatedOngParticipant))
            throw new ForbiddenOperationException("User can't manage events");

        eventRepository.delete(event);
    }

    public EventResponse update(UUID eventId, UpdateEventRequest request, UUID authenticatedUserId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: "+eventId));

        UserProfile authenticatedUserProfile = userProfileRepository.getReferenceById(authenticatedUserId);

        Ong ong = ongRepository.getReferenceById(event.getOng().getId());

        OngParticipant authenticatedOngParticipant = ongParticipantRepository.findById(
                new OngParticipantId(
                        ong, authenticatedUserProfile
                )
        ).orElseThrow(() -> new ForbiddenOperationException("User is not participant of this ONG"));

        if (!canOngParticipantManageEvents(authenticatedOngParticipant))
            throw new ForbiddenOperationException("User can't manage events");

        if (request.title().isPresent())
            event.setTitle(request.title().get());
        if (request.description().isPresent())
            event.setDescription(request.description().get());

        eventRepository.save(event);

        return eventMapper.toResponse(
                eventRepository.getEventSummaryById(eventId, authenticatedUserId)
                    .orElseThrow(() -> new EventNotFoundException("Event not found with id: "+eventId))
        );
    }

    public void likeEvent(UUID eventId, UUID authenticatedUserId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: "+eventId));

        UserProfile user = userProfileRepository.getReferenceById(authenticatedUserId);

        if (eventLikeRepository.existsById(new EventLikeId(event, user)))
            throw new UserAlreadyLikedEventException();

        eventLikeRepository.save(new EventLike(event, user, Instant.now()));
    }

    public void unlikeEvent(UUID eventId, UUID authenticatedUserId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: "+eventId));

        UserProfile user = userProfileRepository.getReferenceById(authenticatedUserId);

        EventLike eventLike = eventLikeRepository.findById(new EventLikeId(event, user))
                .orElseThrow(() -> new UserNotLikedEventYetException());

        eventLikeRepository.delete(new EventLike(event, user, Instant.now()));
    }

    public void viewEvent(UUID eventId, UUID authenticatedUserId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: "+eventId));

        event.setViewsCount(event.getViewsCount()+1);

        eventRepository.save(event);
    }
}
