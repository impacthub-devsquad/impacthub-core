package br.social.impacthub.service;

import br.social.impacthub.exception.ForbiddenOperationException;
import br.social.impacthub.exception.InvalidOngParticipantRoleException;
import br.social.impacthub.exception.OngNotFoundException;
import br.social.impacthub.exception.UserNotExistsException;
import br.social.impacthub.infrastructure.persistence.OngParticipantRepository;
import br.social.impacthub.infrastructure.persistence.OngParticipantRoleRepository;
import br.social.impacthub.infrastructure.persistence.OngRepository;
import br.social.impacthub.infrastructure.persistence.UserProfileRepository;
import br.social.impacthub.model.dto.OngParticipantResponse;
import br.social.impacthub.model.dto.PagedResponse;
import br.social.impacthub.model.dto.UpdateOngParticipantRequest;
import br.social.impacthub.model.entity.*;
import br.social.impacthub.service.mapper.OngParticipantMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class OngParticipantService {
    private final OngRepository ongRepository;
    private final UserProfileRepository userProfileRepository;
    private final OngParticipantRoleRepository ongParticipantRoleRepository;
    private final OngParticipantRepository ongParticipantRepository;
    private final OngParticipantMapper ongParticipantMapper;

    public OngParticipantService(
            OngRepository ongRepository,
            UserProfileRepository userProfileRepository,
            OngParticipantRoleRepository ongParticipantRoleRepository,
            OngParticipantRepository ongParticipantRepository,
            OngParticipantMapper ongParticipantMapper
    ) {
        this.ongRepository = ongRepository;
        this.userProfileRepository = userProfileRepository;
        this.ongParticipantRoleRepository = ongParticipantRoleRepository;
        this.ongParticipantRepository = ongParticipantRepository;
        this.ongParticipantMapper = ongParticipantMapper;
    }

    public PagedResponse<OngParticipantResponse> getAllByOngId(
            UUID ongId,
            Pageable pageable
    ) {
        Page<OngParticipant> page = ongParticipantRepository.findAllByOngId(ongId, pageable);

        return PagedResponse.<OngParticipantResponse>builder()
                .page(page.getNumber())
                .size(page.getSize())
                .isLast(page.isLast())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .content(
                    page.getContent().stream()
                        .map(ongParticipant -> ongParticipantMapper.toResponse(ongParticipant))
                        .toList()
                )
                .build();
    }

    public void deleteParticipant(
            UUID ongId,
            UUID userParticipantId,
            UUID authenticatedUserId
    ){
        Ong ong = ongRepository.findById(ongId)
                .orElseThrow(() -> new OngNotFoundException());

        UserProfile authenticatedUser = userProfileRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        OngParticipant authenticatedUserParticipant = ongParticipantRepository.findById(
                new OngParticipantId(ong, authenticatedUser)
        ).orElseThrow(() -> new UserNotExistsException());

        if (!canUserManageParticipants(authenticatedUserParticipant))
            throw new ForbiddenOperationException("Authenticated user can't manage Ong Participants");

        OngParticipant ongParticipant = ongParticipantRepository.findById(
                new OngParticipantId(ong, userProfileRepository.getReferenceById(userParticipantId))
        ).orElseThrow(() -> new UserNotExistsException("Ong participant not found"));

        if (isUserOwner(ongParticipant))
            throw new ForbiddenOperationException("Owner can't leave the ong, transfer ownership or delete the ong");

        try {
            ongParticipantRepository.delete(ongParticipant);
        } catch (Exception e){
            throw new UserNotExistsException("Participant not exists");
        }
    }

    private boolean canUserManageParticipants(OngParticipant authenticatedUserParticipant) {
        List<Integer> allowedRoles = List.of(
                OngParticipantRole.Values.ADM.getId(),
                OngParticipantRole.Values.OWNER.getId()
        );
        return allowedRoles.contains(authenticatedUserParticipant.getRole().getId());
    }

    private boolean isUserOwner(OngParticipant ongParticipant){
        return ongParticipant.getRole().getId().equals(OngParticipantRole.Values.OWNER.getId());
    }

    public void leaveParticipant(UUID ongId, UUID userParticipantId){
        Ong ong = ongRepository.findById(ongId)
                .orElseThrow(() -> new OngNotFoundException());

        UserProfile user = userProfileRepository.findById(userParticipantId)
                .orElseThrow(() -> new UserNotExistsException("User not found"));

        OngParticipant ongParticipant = ongParticipantRepository.findById(
                new OngParticipantId(ong, user)
        ).orElseThrow(() -> new UserNotExistsException("Participant not exists"));

        if (isUserOwner(ongParticipant))
            throw new ForbiddenOperationException("Owner can't leave the ong, transfer ownership or delete the ong");

        ongParticipantRepository.delete(ongParticipant);
    }

    public void updateParticipant(
            UUID ongId,
            UUID userId,
            @Valid UpdateOngParticipantRequest request,
            UUID authenticatedUserId
    ) {
        if(!canAuthenticatedUserManageOngParticipants(authenticatedUserId, ongId))
            throw new ForbiddenOperationException("Authenticated user can't manage Ong Participants");

        OngParticipant updatedParticipant = ongParticipantRepository.findById(
                new OngParticipantId(ongRepository.getReferenceById(ongId), userProfileRepository.getReferenceById(userId))
        ).orElseThrow(() -> new UserNotExistsException("Ong participant not found"));

        if (isUserOwner(updatedParticipant))
           throw new ForbiddenOperationException("Can't update owner of the ONG");

        if(!isOngParticipantRoleValid(request.role()))
            throw new InvalidOngParticipantRoleException("Invalid role: "+request.role());

        updatedParticipant.setRole(ongParticipantRoleRepository.getReferenceByName(request.role()));
        ongParticipantRepository.save(updatedParticipant);
    }

    private boolean canAuthenticatedUserManageOngParticipants(UUID authenticatedUserId, UUID ongId) {
        OngParticipant authenticatedUserParticipant = ongParticipantRepository.findById(
                new OngParticipantId(ongRepository.getReferenceById(ongId), userProfileRepository.getReferenceById(authenticatedUserId))
        ).orElseThrow(() -> new UserNotExistsException("Participant not exists"));

        return canUserManageParticipants(authenticatedUserParticipant);
    }

    private boolean isOngParticipantRoleValid(String roleName){
        List<String> allowedRoles = Arrays.stream(OngParticipantRole.Values.values())
                .map(roleValue -> roleValue.getName())
                .toList();
        return allowedRoles.contains(roleName);
    }
}
