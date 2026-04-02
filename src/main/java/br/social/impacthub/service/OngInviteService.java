package br.social.impacthub.service;

import br.social.impacthub.exception.*;
import br.social.impacthub.infrastructure.persistence.*;
import br.social.impacthub.model.dto.CreateOngInviteRequest;
import br.social.impacthub.model.dto.OngInviteResponse;
import br.social.impacthub.model.dto.PagedResponse;
import br.social.impacthub.model.entity.*;
import br.social.impacthub.service.mapper.OngInviteMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class OngInviteService {
    private OngParticipantRoleRepository ongParticipantRoleRepository;
    private OngInviteMapper ongInviteMapper;
    private OngInviteRepository ongInviteRepository;
    private OngParticipantRepository ongParticipantRepository;
    private OngRepository ongRepository;
    private UserProfileRepository userProfileRepository;

    public OngInviteService(
            OngParticipantRoleRepository ongParticipantRoleRepository,
            OngInviteMapper ongInviteMapper,
            OngInviteRepository ongInviteRepository,
            OngParticipantRepository ongParticipantRepository,
            OngRepository ongRepository,
            UserProfileRepository userProfileRepository
    ) {
        this.ongParticipantRoleRepository = ongParticipantRoleRepository;
        this.ongInviteMapper = ongInviteMapper;
        this.ongInviteRepository = ongInviteRepository;
        this.ongParticipantRepository = ongParticipantRepository;
        this.ongRepository = ongRepository;
        this.userProfileRepository = userProfileRepository;
    }

    public PagedResponse<OngInviteResponse> getAllByOngId(UUID ongId, UUID authenticatedUserId, Pageable pageable) {
        var ongParticipant = ongParticipantRepository.findById(new OngParticipantId(
                ongRepository.getReferenceById(ongId),
                userProfileRepository.getReferenceById(authenticatedUserId)
        )).orElseThrow(() -> new OngParticipantNotFoundException());

        OngParticipantRole role = ongParticipant.getRole();

        if (!canUserManageOngInvites(role))
            throw new ForbiddenOperationException("Authenticated user hasn't permission to access this resource");

        Page<OngInvite> page = ongInviteRepository.findAllByOng_OngId(ongId, pageable);

        return new PagedResponse<OngInviteResponse>(
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.isLast(),
                page.getContent().stream()
                        .map(ongInvite -> ongInviteMapper.toResponse(ongInvite))
                        .toList()
        );
    }

    private boolean canUserManageOngInvites(OngParticipantRole userRole){
        List<Integer> allowedRoles = List.of(
                OngParticipantRole.Values.OWNER.getId(),
                OngParticipantRole.Values.ADM.getId()
        );
        return allowedRoles.contains(userRole.getId());
    }

    @Transactional
    public void create(@Valid CreateOngInviteRequest request, UUID ongId, UUID authenticatedUserId) {
        Ong ong = ongRepository.findById(ongId)
                .orElseThrow(() -> new OngNotFoundException());

        UserProfile authenticatedUser = userProfileRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new UserNotExistsException("Authenticated user not found"));

        UserProfile user = userProfileRepository.findById(request.userID())
                .orElseThrow(() -> new UserNotExistsException("User not found"));

        OngParticipantRole authenticatedUserRole = ongParticipantRepository.findById(new OngParticipantId(ong, authenticatedUser)).
                orElseThrow(() -> new OngParticipantNotFoundException("Ong Participant not found")).getRole();

        if (!canUserManageOngInvites(authenticatedUserRole))
            throw new ForbiddenOperationException("Authenticated user hasn't permission to create invites");

        OngParticipantRole role = ongParticipantRoleRepository.getReferenceByName(request.role());

        OngInvite newInvite = new OngInvite(user, ong, role, Instant.now());

        ongInviteRepository.save(newInvite);
    }

    public void delete(UUID ongId, UUID userId, UUID authenticatedUserId) {
        Ong ong = ongRepository.findById(ongId)
                .orElseThrow(() -> new OngNotFoundException());

        UserProfile authenticatedUser = userProfileRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new UserNotExistsException("Authenticated user not found"));

        OngParticipantRole authenticatedUserRole = ongParticipantRepository.findById(new OngParticipantId(ong, authenticatedUser)).
                orElseThrow(() -> new OngParticipantNotFoundException()).getRole();

        if (!canUserManageOngInvites(authenticatedUserRole))
            throw new ForbiddenOperationException("Authenticated user hasn't permission to create invites");

        UserProfile user = userProfileRepository.findById(userId)
                .orElseThrow(() -> new UserNotExistsException("User not found"));

        OngInviteId inviteId = new OngInviteId(ong, user);

        ongInviteRepository.deleteById(inviteId);
    }

    @Transactional
    public void accept(UUID ongId, UUID authenticatedUserId) {
        Ong ong = ongRepository.findById(ongId)
                .orElseThrow(() -> new OngNotFoundException());

        UserProfile user = userProfileRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new UserNotExistsException("User not found"));

        OngInviteId inviteId = new OngInviteId(ong, user);

        OngInvite invite = ongInviteRepository.findById(inviteId)
                .orElseThrow(() -> new OngInviteNotFoundException("Authenticated user is not invited to this ONG"));

        OngParticipant newOngParticipant = new OngParticipant(ong, user, invite.getRole());

        ongInviteRepository.delete(invite);
        ongParticipantRepository.save(newOngParticipant);
    }

    public void recuse(UUID ongId, UUID authenticatedUserId) {
        Ong ong = ongRepository.findById(ongId)
                .orElseThrow(() -> new OngNotFoundException());

        UserProfile user = userProfileRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new UserNotExistsException("User not found"));

        OngInviteId inviteId = new OngInviteId(ong, user);

        if (!ongInviteRepository.existsById(inviteId))
            throw new OngInviteNotFoundException("Authenticated user is not invited to this ONG");

        ongInviteRepository.deleteById(inviteId);
    }
}
