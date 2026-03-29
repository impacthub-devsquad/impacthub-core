package br.social.impacthub.infrastructure.web;

import br.social.impacthub.model.dto.*;
import br.social.impacthub.service.OngInviteService;
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
public class OngController {
    private OngInviteService ongInviteService;
    private OngService ongService;
    private AuthService authService;

    public OngController(OngInviteService ongInviteService, OngService ongService, AuthService authService) {
        this.ongInviteService = ongInviteService;
        this.ongService = ongService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<StandardResponse<PagedResponse<OngResponse>>> getAll(Pageable pageable){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success(
                            ongService.getAll(pageable)
                        )
                );
    }

    @GetMapping("/{ongId}")
    public ResponseEntity<StandardResponse<OngResponse>> getByID(@PathVariable UUID ongId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success(
                                ongService.getById(ongId)
                        )
                );
    }


    @PostMapping
    public ResponseEntity<StandardResponse<OngResponse>> create(@Valid @RequestBody CreateOngRequest request){
        UUID userId = authService.getAuthenticatedUser().userId();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        StandardResponse.success(
                                ongService.create(request, userId)
                        )
                );
    }

    @PatchMapping("/{ongId}")
    public ResponseEntity<StandardResponse<OngResponse>> update(@Valid @RequestBody UpdateOngRequest request){
        UUID userId = authService.getAuthenticatedUser().userId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success(
                                ongService.update(request, userId)
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
        ongService.followOng(authenticatedUserId, ongId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        StandardResponse.success()
                );
    }

    @DeleteMapping("/{ongId}/followers/me")
    public ResponseEntity<StandardResponse<Void>> unfollowOng(@PathVariable UUID ongId){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();
        ongService.unfollowOng(authenticatedUserId, ongId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success()
                );
    }
}
