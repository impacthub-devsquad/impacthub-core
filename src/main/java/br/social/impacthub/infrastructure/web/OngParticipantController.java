package br.social.impacthub.infrastructure.web;

import br.social.impacthub.infrastructure.web.docs.OngParticipantControllerDocs;
import br.social.impacthub.model.dto.*;
import br.social.impacthub.service.OngInviteService;
import br.social.impacthub.service.OngParticipantService;
import br.social.impacthub.service.security.AuthService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ongs")
public class OngParticipantController implements OngParticipantControllerDocs {
    private final AuthService authService;
    private final OngInviteService ongInviteService;
    private final OngParticipantService ongParticipantService;

    public OngParticipantController(
            AuthService authService,
            OngInviteService ongInviteService,
            OngParticipantService ongParticipantService
    ) {
        this.authService = authService;
        this.ongInviteService = ongInviteService;
        this.ongParticipantService = ongParticipantService;
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

    @DeleteMapping("/{ongId}/invites/{userId}")
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
            @PathVariable UUID ongId
    ){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();
        ongInviteService.recuse(ongId, authenticatedUserId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success()
                );
    }

    @GetMapping("/{ongId}/participants")
    public ResponseEntity<StandardResponse<PagedResponse<OngParticipantResponse>>> getAllParticipants(
            @PathVariable UUID ongId,
            Pageable pageable
    ){
        PagedResponse<OngParticipantResponse> response = ongParticipantService.getAllByOngId(ongId, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success(response)
                );
    }

    @DeleteMapping("/{ongId}/participants/{userId}")
    public ResponseEntity<StandardResponse<Void>> deleteParticipant(
            @PathVariable UUID ongId,
            @PathVariable UUID userId
    ){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();
        ongParticipantService.deleteParticipant(ongId, userId, authenticatedUserId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success()
                );
    }

    @DeleteMapping("/{ongId}/participants/me")
    public ResponseEntity<StandardResponse<Void>> leaveParticipant(
            @PathVariable UUID ongId
    ){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();
        ongParticipantService.leaveParticipant(ongId, authenticatedUserId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success()
                );
    }

    @PatchMapping("/{ongId}/participants/{userId}/")
    public ResponseEntity<StandardResponse<Void>> updateParticipant(
            @PathVariable UUID ongId,
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateOngParticipantRequest request
    ){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();
        ongParticipantService.updateParticipant(ongId, userId, request, authenticatedUserId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success()
                );
    }

    @GetMapping("/{ongId}/participants/{participantId}")
    public ResponseEntity<StandardResponse<OngParticipantResponse>> getParticipant(
            @PathVariable UUID ongId,
            @PathVariable UUID participantId
    ){
        OngParticipantResponse response = ongParticipantService.getById(ongId, participantId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success(response)
                );
    }
}
