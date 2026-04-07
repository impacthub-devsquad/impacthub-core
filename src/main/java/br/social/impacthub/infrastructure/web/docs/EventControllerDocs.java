//package br.social.impacthub.infrastructure.web.docs;
//
//import br.social.impacthub.model.dto.*;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.ResponseEntity;
//
//import java.util.UUID;
//
//@Tag(name = "Events")
//public interface EventControllerDocs {
//
//    @Operation(
//            summary = "Get all events"
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Events successfully retrieved."),
//            @ApiResponse(responseCode = "404", description = "No events found in the database.")
//    })
//    ResponseEntity<StandardResponse<PagedResponse<EventSummaryResponse>>> getAll(Pageable pageable);
//
//    @Operation(
//            summary = "Get event by ID"
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Event successfully retrieved."),
//            @ApiResponse(responseCode = "404", description = "Event not found.")
//    })
//    ResponseEntity<StandardResponse<EventSummaryResponse>> getByID(UUID eventId);
//
//    @Operation(
//            summary = "Get all events of an ONG"
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Events successfully retrieved."),
//            @ApiResponse(responseCode = "404", description = "ONG not found or no events available.")
//    })
//    ResponseEntity<StandardResponse<PagedResponse<EventSummaryResponse>>> getByOngId(UUID ongId, Pageable pageable);
//
//    @SecurityRequirement(name = "bearerAuth")
//    @Operation(
//            summary = "Create a new event"
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "201", description = "Event successfully created."),
//            @ApiResponse(responseCode = "400", description = "Invalid request data")
//    })
//    ResponseEntity<StandardResponse<EventSummaryResponse>> create(CreateEventRequest request);
//
//    @SecurityRequirement(name = "bearerAuth")
//    @Operation(
//            summary = "Update event information"
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Event successfully updated."),
//            @ApiResponse(responseCode = "400", description = "Invalid request data"),
//            @ApiResponse(responseCode = "404", description = "Event not found."),
//            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have permission to update this event")
//    })
//    ResponseEntity<StandardResponse<EventResponse>> update(UUID eventId, UpdateEventRequest request);
//
//    @SecurityRequirement(name = "bearerAuth")
//    @Operation(
//            summary = "Delete event"
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "204", description = "Event successfully deleted."),
//            @ApiResponse(responseCode = "404", description = "Event not found."),
//            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have permission to delete this event")
//    })
//    ResponseEntity<StandardResponse<Void>> delete(UUID eventId);
//
//    @Operation(
//            summary = "Like an event"
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Event liked successfully"),
//            @ApiResponse(responseCode = "404", description = "Event not found.")
//    })
//    ResponseEntity<StandardResponse<PagedResponse<EventLikeResponse>>> getEventLikes(UUID eventId, Pageable pageable);
//
//    @SecurityRequirement(name = "bearerAuth")
//    @Operation(
//            summary = "Unlike an event"
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Successfully unliked event."),
//            @ApiResponse(responseCode = "404", description = "Event not found or like not found.")
//    })
//    ResponseEntity<StandardResponse<Void>> unlikeEvent(UUID eventId);
//
//    @SecurityRequirement(name = "bearerAuth")
//    @Operation(
//            summary = "View event"
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Event view recorded successfully."),
//            @ApiResponse(responseCode = "404", description = "Event not found.")
//    })
//    ResponseEntity<StandardResponse<Void>> viewEvent(UUID eventId);
//}
