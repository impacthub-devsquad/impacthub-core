package br.social.impacthub.infrastructure.web.docs;

import br.social.impacthub.model.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Tag(name = "Events")
public interface EventControllerDocs {
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get all events", description = "Retrieve a paginated list of all events visible to the authenticated user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Events retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class)
                    )
            )
    })
    ResponseEntity<StandardResponse<PagedResponse<EventResponse>>> getAllEvents(Pageable pageable);

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get event by ID", description = "Retrieve detailed information about a specific event")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Event retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Event not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class)
                    )
            )
    })
    ResponseEntity<StandardResponse<EventResponse>> getEventById(UUID eventId);

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get all events by ONG ID", description = "Retrieve a paginated list of all events associated with a specific ONG")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "ONG events retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "ONG not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class)
                    )
            )
    })
    ResponseEntity<StandardResponse<PagedResponse<EventResponse>>> getAllEventsByOng(UUID ongId, Pageable pageable);

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create event", description = "Create a new event for an ONG managed by the authenticated user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Event created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - User is not an admin of the ONG",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "ONG not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class)
                    )
            )
    })
    ResponseEntity<StandardResponse<EventResponse>> createEvent(CreateEventRequest request);

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete event by ID", description = "Delete an event (only the ONG admin can delete)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Event deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - User is not an admin of the ONG",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Event not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class)
                    )
            )
    })
    ResponseEntity<StandardResponse<Void>> deleteEvent(UUID eventId);

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update event by ID", description = "Update event details (only the ONG admin or who Publish the Event can update)")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Event updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - User can't manage the Event",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Event not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class)
                    )
            )
    })
    ResponseEntity<StandardResponse<EventResponse>> updateEvent(UUID eventId, UpdateEventRequest request);

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Like event by ID", description = "Add a like to an event from the authenticated user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Event liked successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Event not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", // TODO: implement 409 Conflict
                    description = "Bad Request - User has already liked this event",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class)
                    )
            )
    })
    ResponseEntity<StandardResponse<Void>> likeEvent(UUID eventId);

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Unlike event by ID", description = "Remove a like from an event from the authenticated user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Event unlike successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", // TODO: implement 409 Conflict
                    description = "Bad Request - User hasn't liked this event yet",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Event not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class)
                    )
            )
    })
    ResponseEntity<StandardResponse<Void>> unlikeEvent(UUID eventId);

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "View event by ID", description = "Record a view event from the authenticated user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Event view recorded successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Event not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class)
                    )
            )
    })
    ResponseEntity<StandardResponse<Void>> viewEvent(UUID eventId);
}
