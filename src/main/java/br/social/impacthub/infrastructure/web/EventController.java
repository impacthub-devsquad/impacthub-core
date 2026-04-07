package br.social.impacthub.infrastructure.web;

import br.social.impacthub.model.dto.*;
import br.social.impacthub.service.EventService;
import br.social.impacthub.service.security.AuthService;
import com.sun.jdi.request.EventRequest;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class EventController {
    private final EventService eventService;
    private final AuthService authService;

    public EventController(EventService eventService, AuthService authService) {
        this.eventService = eventService;
        this.authService = authService;
    }

    @GetMapping("/events")
    public ResponseEntity<StandardResponse<PagedResponse<EventResponse>>> getAllEvents(Pageable pageable){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(StandardResponse.success(
                        eventService.getAll(authenticatedUserId, pageable)
                ));
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<StandardResponse<EventResponse>> getEventById(@PathVariable UUID eventId){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(StandardResponse.success(
                        eventService.getByEventId(eventId, authenticatedUserId)
                ));
    }

    @GetMapping("/ongs/{ongId}/events")
    public ResponseEntity<StandardResponse<PagedResponse<EventResponse>>> getAllEventsByOng(
            @PathVariable UUID ongId,
            Pageable pageable
    ){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(StandardResponse.success(
                        eventService.getAllByOngId(ongId, authenticatedUserId, pageable)
                ));
    }

    @PostMapping("/events")
    public ResponseEntity<StandardResponse<EventResponse>> createEvent(@Valid @RequestBody CreateEventRequest request){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(StandardResponse.success(
                        eventService.create(request, authenticatedUserId)
                ));
    }

    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<StandardResponse<Void>> deleteEvent(@PathVariable UUID eventId){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();

        eventService.deleteByEventId(eventId, authenticatedUserId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(StandardResponse.success());
    }

    @PatchMapping("/events/{eventId}")
    public ResponseEntity<StandardResponse<EventResponse>> updateEvent(@PathVariable UUID eventId, @Valid @RequestBody UpdateEventRequest request){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(StandardResponse.success(
                        eventService.update(eventId, request, authenticatedUserId)
                ));
    }

//    TODO: implement this
//    @GetMapping("/events/{eventId}/likes")
//    public ResponseEntity<StandardResponse<PagedResponse<UserProfileResponse>>> getLikes(@PathVariable UUID eventId){
//        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();
//
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(StandardResponse.success(
//                        eventService.getEventLikes(eventId)
//                ));
//    }

    @PostMapping("/events/{eventId}/likes/me")
    public ResponseEntity<StandardResponse<Void>> likeEvent(@PathVariable UUID eventId){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();

        eventService.likeEvent(eventId, authenticatedUserId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(StandardResponse.success());
    }

    @DeleteMapping("/events/{eventId}/likes/me")
    public ResponseEntity<StandardResponse<Void>> unlikeEvent(@PathVariable UUID eventId){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();

        eventService.unlikeEvent(eventId, authenticatedUserId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(StandardResponse.success());
    }

    @PostMapping("/events/{eventId}/views/me")
    public ResponseEntity<StandardResponse<Void>> viewEvent(@PathVariable UUID eventId){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();

        eventService.viewEvent(eventId, authenticatedUserId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(StandardResponse.success());
    }
}