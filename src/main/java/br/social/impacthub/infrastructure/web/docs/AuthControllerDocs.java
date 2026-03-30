package br.social.impacthub.infrastructure.web.docs;

import br.social.impacthub.model.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

@Tag(name = "Authentication")
public interface AuthControllerDocs {
    @Operation(
            summary = "Register a new user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Successfully registration",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Malformed data",
                                            value =
                                            """
                                            {
                                                "status": "fail",
                                                "message": "Malformed data",
                                                "data": {
                                                    "password": "Length must be between 8 and 50",
                                                    "email": "Must be a well-formed email address",
                                                    "username": "Must not be blank"
                                                }
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "Invalid email address",
                                            value =
                                            """
                                            {
                                                "status": "fail",
                                                "message": "Invalid email address"
                                            }
                                            """
                                    )
                            }
                    )
            )
    })
    ResponseEntity<Void> register(RegisterUserRequest request);

    @Operation(
            summary = "Login with email and password",
            description = "Login user and retrieve JWT tokens (access and refresh)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully login",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class),
                            examples = {
                                    @ExampleObject(
                                            """
                                            {
                                                "status": "success",
                                                "data": {
                                                    "accessToken": "...",
                                                    "refreshToken": "..."
                                                }
                                            }
                                            """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class),
                            examples = {
                                    @ExampleObject(
                                            """
                                            {
                                                "status": "fail",
                                                "message": "Malformed data",
                                                "data": {
                                                    "password": "Length must be between 8 and 50",
                                                    "email": "Must be a well-formed email address"
                                                }
                                            }
                                            """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid credentials",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class),
                            examples = {
                                    @ExampleObject(
                                            """
                                            {
                                              "status": "fail",
                                              "message": "Wrong email or password"
                                            }
                                            """
                                    )
                            }
                    )
            )
    })
    ResponseEntity<StandardResponse<LoginResponse>> login(LoginRequest request);

    @Operation(
            summary = "Refresh token",
            description = "Refresh JWT Tokens using a valid refresh token"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully refreshed tokens",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class),
                            examples = {
                                    @ExampleObject(
                                            """
                                            {
                                                "status": "success",
                                                "data": {
                                                    "accessToken": "...",
                                                    "refreshToken": "..."
                                                }
                                            }
                                            """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Refresh token expired",
                                            value =
                                            """
                                            {
                                                "status": "fail",
                                                "message": "Refresh token expired"
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "Invalid refresh token",
                                            value =
                                            """
                                            {
                                                "status": "fail",
                                                "message": "Invalid refresh token"
                                            }
                                            """
                                    )
                            }
                    )
            )
    })
    ResponseEntity<LoginResponse> refresh(RefreshRequest request);
}
