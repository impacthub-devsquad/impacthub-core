package br.social.impacthub.infrastructure.web;

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
public class OngParticipantController {
    private OngParticipantService ongParticipantService;
    private AuthService authService;
    private OngInviteService ongInviteService;

    public OngParticipantController(OngParticipantService ongParticipantService, AuthService authService) {
        this.ongParticipantService = ongParticipantService;
        this.authService = authService;
    }

    @GetMapping({"/{ongId}/participants"})
    public ResponseEntity<StandardResponse<PagedResponse<OngParticipantResponse>>> getAllByOng(
            @PathVariable UUID ongId,
            Pageable pageable
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success(
                            ongParticipantService.getAll(ongId, pageable)
                        )
                );
    }

    @GetMapping("/{ongId}/participants/{participantId}")
    public ResponseEntity<StandardResponse<OngParticipantResponse>> getByParticipantId(
            @PathVariable UUID ongId,
            @PathVariable UUID userId
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success(
                                ongParticipantService.getByUserId(ongId, userId)
                        )
                );
    }

    @DeleteMapping("/{ongId}/participants/me")
    public ResponseEntity<StandardResponse<Void>> leaveParticipant(
            @PathVariable UUID ongId
    ){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();

        ongParticipantService.delete(ongId, authenticatedUserId, authenticatedUserId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success()
                );
    }

    @DeleteMapping("/{ongId}/participants/{userId}")
    public ResponseEntity<StandardResponse<Void>> removeParticipant(
            @PathVariable UUID ongId,
            @PathVariable UUID userId
    ){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();

        ongParticipantService.delete(ongId, userId, authenticatedUserId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success()
                );
    }

    @PostMapping("/{ongId}/participants/invites")
    public ResponseEntity<StandardResponse<Void>> inviteParticipant(
            @PathVariable UUID ongId,
            @Valid @RequestBody CreateOngParticipantInviteRequest request
    ){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();

        ongParticipantService.inviteParticipant(ongId, request.userId(), request.roleId(), authenticatedUserId);

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
            )
    {
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();

        ongParticipantService.deleteInvite(ongId, userId, authenticatedUserId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success()
                );
    }

    @PostMapping("/{ongId}/invites/{inviteId}/accept")
    public ResponseEntity<StandardResponse<Void>> acceptInvite(
            @PathVariable UUID ongId,
            @PathVariable UUID inviteId
    ){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();
        ongInviteService.accept(inviteId, authenticatedUserId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success()
                );
    }

    @PostMapping("/{ongId}/invites/{inviteId}/recuse")
    public ResponseEntity<StandardResponse<Void>> recuseInvite(
            @PathVariable UUID ongId,
            @PathVariable UUID inviteId
    ){
        UUID authenticatedUserId = authService.getAuthenticatedUser().userId();
        ongInviteService.recuse(inviteId, authenticatedUserId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        StandardResponse.success()
                );
    }
}
