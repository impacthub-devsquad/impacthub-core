package br.social.impacthub.infrastructure.web.docs;

import br.social.impacthub.model.dto.PagedResponse;
import br.social.impacthub.model.dto.StandardResponse;
import br.social.impacthub.model.dto.UpdateUserRequest;
import br.social.impacthub.model.dto.UserProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Users")
public interface UserControllerDocs {
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get all users", description = "Retrieve a paginated list of all users with optional search query")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Users retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class)
                    )
            )
    })
    ResponseEntity<StandardResponse<PagedResponse<UserProfileResponse>>> getAll(String query, Pageable pageable);

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get authenticated user", description = "Retrieve profile information of the currently authenticated user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Authenticated user retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class)
                    )
            )
    })
    ResponseEntity<StandardResponse<UserProfileResponse>> getAuthenticatedUser();

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get user by ID", description = "Retrieve profile information of a specific user by their ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class)
                    )
            )
    })
    ResponseEntity<StandardResponse<UserProfileResponse>> getUserById(UUID id);

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update authenticated user", description = "Update profile information of the currently authenticated user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User updated successfully",
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
            )
    })
    ResponseEntity<StandardResponse<UserProfileResponse>> updateAuthenticatedUser(UpdateUserRequest request);

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete authenticated user", description = "Delete the account of the currently authenticated user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class)
                    )
            )
    })
    ResponseEntity<StandardResponse<Void>> deleteAuthenticatedUser();
}
