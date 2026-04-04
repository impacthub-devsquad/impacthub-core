package br.social.impacthub.infrastructure.web;

import br.social.impacthub.infrastructure.web.docs.OngControllerDocs;
import br.social.impacthub.model.dto.*;
import br.social.impacthub.service.OngService;
import br.social.impacthub.service.security.AuthService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ongs")
public class OngController implements OngControllerDocs {
    private final OngService ongService;
    private final AuthService authService;

    public OngController(OngService ongService, AuthService authService) {
        this.ongService = ongService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<StandardResponse<PagedResponse<OngSummaryResponse>>> getAll(
            @RequestParam(required = false, defaultValue = "") String category,
            Pageable pageable
    ){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();

        PagedResponse<OngSummaryResponse> response;

        if(category.isBlank()){
            response = ongService.getAll(authenticatedUserId, pageable);
        } else {
            response = ongService.getAllByCategory(authenticatedUserId, category, pageable);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success(response)
                );
    }

    @GetMapping("/{ongId}")
    public ResponseEntity<StandardResponse<OngSummaryResponse>> getByID(
            @PathVariable UUID ongId
    ){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success(
                                ongService.getById(ongId, authenticatedUserId)
                        )
                );
    }

    @PostMapping
    public ResponseEntity<StandardResponse<OngSummaryResponse>> create(
            @Valid @RequestBody CreateOngRequest request
    ){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        StandardResponse.success(
                                ongService.create(request, authenticatedUserId)
                        )
                );
    }

    @PatchMapping("/{ongId}")
    public ResponseEntity<StandardResponse<OngResponse>> update(
            @PathVariable UUID ongId,
            @Valid @RequestBody UpdateOngRequest request
    ){
        UUID userId = authService.getAuthenticatedUser().userId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success(
                                ongService.update(ongId, request, userId)
                        )
                );
    }

    @DeleteMapping("/{ongId}")
    public ResponseEntity<StandardResponse<Void>> delete(@PathVariable UUID ongId){
        UUID userId = authService.getAuthenticatedUser().userId();

        ongService.delete(ongId, userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success()
                );
    }

    @PostMapping("/{ongId}/followers/me")
    public ResponseEntity<StandardResponse<Void>> followOng(@PathVariable UUID ongId){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();
        ongService.followOng(ongId, authenticatedUserId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        StandardResponse.success()
                );
    }

    @DeleteMapping("/{ongId}/followers/me")
    public ResponseEntity<StandardResponse<Void>> unfollowOng(@PathVariable UUID ongId){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();
        ongService.unfollowOng(ongId, authenticatedUserId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success()
                );
    }
}
