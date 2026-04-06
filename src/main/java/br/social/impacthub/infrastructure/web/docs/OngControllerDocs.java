package br.social.impacthub.infrastructure.web.docs;

import br.social.impacthub.model.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Ongs")
public interface OngControllerDocs {

    @Operation(
            summary = "Get all ONGs"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ONGs successfully retrieved."),
            @ApiResponse(responseCode = "404", description = "No ONGs found in the database.")
    })
    ResponseEntity<StandardResponse<PagedResponse<OngSummaryResponse>>> getAll(String category, Pageable pageable);

    @Operation(
            summary = "Get ONG by ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ONG successfully retrieved."),
            @ApiResponse(responseCode = "404", description = "ONG not found.")
    })
    ResponseEntity<StandardResponse<OngSummaryResponse>> getByID(UUID ongId);

    @Operation(
            summary = "Create a new ONG"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "ONG successfully created."),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    ResponseEntity<StandardResponse<OngSummaryResponse>> create(CreateOngRequest request);

    @Operation(
            summary = "Update ONG information"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ONG successfully updated."),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    ResponseEntity<StandardResponse<OngResponse>> update(UUID ongId, UpdateOngRequest request);

    @Operation(
            summary = "Delete ONG"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ONG successfully deleted."),
            @ApiResponse(responseCode = "404", description = "ONG not found."),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have permission to delete this ONG")
    })
    ResponseEntity<StandardResponse<Void>> delete(UUID ongId);

    @Operation(
            summary = "Follow ONG"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successfully followed ONG."),
            @ApiResponse(responseCode = "400", description = "Invalid request data - ONG not found")
    })
    ResponseEntity<StandardResponse<Void>> followOng(UUID ongId);

    @Operation(
            summary = "Unfollow ONG"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully unfollowed ONG."),
            @ApiResponse(responseCode = "400", description = "Invalid request data - User is not following this ONG")
    })
    ResponseEntity<StandardResponse<Void>> unfollowOng(UUID ongId);
}
