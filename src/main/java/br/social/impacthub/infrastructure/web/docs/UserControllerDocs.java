package br.social.impacthub.infrastructure.web.docs;

import br.social.impacthub.model.dto.PagedResponse;
import br.social.impacthub.model.dto.StandardResponse;
import br.social.impacthub.model.dto.UpdateUserRequest;
import br.social.impacthub.model.dto.UserProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Users")
public interface UserControllerDocs {

    @Operation(
            summary = "Get all users"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users successfully retrieved."),
            @ApiResponse(responseCode = "404", description = "No users found in the database.")
    })
    ResponseEntity<StandardResponse<PagedResponse<UserProfileResponse>>> getAll();

    @Operation(
            summary = "Get authenticated user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User successfully retrieved."),
            @ApiResponse(responseCode = "404", description = "User not found in the database.")
    })
    ResponseEntity<StandardResponse<UserProfileResponse>> getAuthenticatedUser();

    @Operation(
            summary = "Get user by ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User successfully retrieved."),
            @ApiResponse(responseCode = "404", description = "User not found."),
            @ApiResponse(responseCode = "400", description = "Invalid user ID.")
    })
    ResponseEntity<StandardResponse<UserProfileResponse>> getUserById(UUID id);

    @Operation(
            summary = "Update user information"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User successfully updated."),
            @ApiResponse(responseCode = "404", description = "User not found."),
            @ApiResponse(responseCode = "400", description = "Invalid user ID.")
    })
    ResponseEntity<StandardResponse<UserProfileResponse>> updateAuthenticatedUser(UpdateUserRequest request);

    @Operation(
            summary = "Delete authenticated user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User successfully deleted."),
            @ApiResponse(responseCode = "202", description = "Delete operation accepted."),
            @ApiResponse(responseCode = "204", description = "No content to return."),
            @ApiResponse(responseCode = "404", description = "User not found."),
            @ApiResponse(responseCode = "400", description = "Validation error. Invalid user ID.")
    })
    ResponseEntity<StandardResponse<Void>> deleteAuthenticatedUser();
}
