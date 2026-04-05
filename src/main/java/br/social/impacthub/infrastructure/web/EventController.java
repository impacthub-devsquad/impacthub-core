package br.social.impacthub.infrastructure.web;

import br.social.impacthub.infrastructure.web.docs.EventControllerDocs;
import br.social.impacthub.model.dto.*;
import br.social.impacthub.service.EventService;
import br.social.impacthub.service.security.AuthService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class EventController implements EventControllerDocs {
    private final EventService eventService;
    private final AuthService authService;

    public EventController(EventService eventService, AuthService authService) {
        this.eventService = eventService;
        this.authService = authService;
    }

    @GetMapping("/events")
    public ResponseEntity<StandardResponse<PagedResponse<EventSummaryResponse>>> getAll(Pageable pageable) {
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success(
                                eventService.getAll(authenticatedUserId, pageable)
                        )
                );
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<StandardResponse<EventSummaryResponse>> getByID(
            @PathVariable UUID eventId
    ) {
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success(
                                eventService.getById(eventId, authenticatedUserId)
                        )
                );
    }

    @GetMapping("ongs/{ongId}/events")
    public ResponseEntity<StandardResponse<PagedResponse<EventSummaryResponse>>> getByOngId(
            @PathVariable UUID ongId,
            Pageable pageable
    ) {
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success(
                                eventService.getEventsByOng(ongId, authenticatedUserId, pageable)
                        )
                );
    }

    @PostMapping("/events")
    public ResponseEntity<StandardResponse<EventSummaryResponse>> create(
            @RequestBody @Valid CreateEventRequest request
    ) {
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        StandardResponse.success(
                                eventService.create(request, authenticatedUserId)
                        )
                );
    }

    @PatchMapping("/events/{eventId}")
    public ResponseEntity<StandardResponse<EventResponse>> update(
            @PathVariable UUID eventId,
            @RequestBody @Valid UpdateEventRequest request
    ) {
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success(
                                eventService.update(eventId, request, authenticatedUserId)
                        )
                );
    }

    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<StandardResponse<Void>> delete(
            @PathVariable UUID eventId
    ) {
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();

        eventService.delete(eventId, authenticatedUserId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(
                        StandardResponse.success()
                );
    }

    @GetMapping("events/{eventId}/likes")
    public ResponseEntity<StandardResponse<PagedResponse<EventLikeResponse>>> getEventLikes(
            @PathVariable UUID eventId,
            Pageable pageable
    ) {
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success(
                                eventService.getEventLikes(eventId, authenticatedUserId, pageable)
                        )
                );
    }

    @DeleteMapping("/events/{eventId}/likes")
    public ResponseEntity<StandardResponse<Void>> unlikeEvent(
            @PathVariable UUID eventId
    ) {
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();

        eventService.unlikeEvent(eventId, authenticatedUserId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success()
                );

    }

    @PostMapping("/events/{eventId}/views")
    public ResponseEntity<StandardResponse<Void>> viewEvent(
            @PathVariable UUID eventId
    ) {
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();

        eventService.viewEvent(eventId, authenticatedUserId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success()
                );

    }
}