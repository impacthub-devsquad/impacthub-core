package br.social.impacthub.infrastructure.web;

import br.social.impacthub.model.dto.CreateOngInviteRequest;
import br.social.impacthub.model.dto.StandardResponse;
import br.social.impacthub.service.OngInviteService;
import br.social.impacthub.service.security.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ongs")
public class OngParticipantController {
    private final AuthService authService;
    private final OngInviteService ongInviteService;

    public OngParticipantController(AuthService authService, OngInviteService ongInviteService) {
        this.authService = authService;
        this.ongInviteService = ongInviteService;
    }

    @PostMapping("/{ongId}/invites")
    public ResponseEntity<StandardResponse<Void>> inviteUser(
            @PathVariable UUID ongId,
            @Valid @RequestBody CreateOngInviteRequest request
    ){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();

        ongInviteService.create(request, ongId, authenticatedUserId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        StandardResponse.success()
                );
    }

    @PostMapping("/{ongId}/invites/{userId}")
    public ResponseEntity<StandardResponse<Void>> deleteInvite(
            @PathVariable UUID ongId,
            @PathVariable UUID userId
    ){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();
        ongInviteService.delete(userId, ongId, authenticatedUserId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success()
                );
    }

    @PostMapping("/{ongId}/invites/me/accept")
    public ResponseEntity<StandardResponse<Void>> acceptInvite(
            @PathVariable UUID ongId
    ){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();
        ongInviteService.accept(ongId, authenticatedUserId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success()
                );
    }

    @PostMapping("/{ongId}/invites/me/recuse")
    public ResponseEntity<StandardResponse<Void>> recuseInvite(
            @PathVariable UUID ongId,
            @PathVariable UUID userId
    ){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();
        ongInviteService.recuse(ongId, userId, authenticatedUserId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success()
                );
    }
}
