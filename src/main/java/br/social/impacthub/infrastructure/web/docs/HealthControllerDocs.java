package br.social.impacthub.infrastructure.web.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Tag(name = "Health")
public interface HealthControllerDocs {
    @Operation(
            summary = "Check health of API"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "API is healthy",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            """
                                            {
                                                "status": "UP",
                                                "timestamp": "2026-01-01T12:00:00Z"
                                            }
                                            """
                                    )
                            }
                    )
            )
    })
    ResponseEntity<Map<String, String>> check();
}
