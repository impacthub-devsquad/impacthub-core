package br.social.impacthub.service;

import br.social.impacthub.exception.*;
import br.social.impacthub.infrastructure.persistence.*;
import br.social.impacthub.model.dto.*;
import br.social.impacthub.model.entity.*;
import br.social.impacthub.service.mapper.OngMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class OngService {
    private final OngMapper ongMapper;
    private final OngRepository ongRepository;
    private final UserProfileRepository userProfileRepository;
    private final OngFollowerRepository ongFollowerRepository;
    private final OngCategoryRepository ongCategoryRepository;
    private final OngParticipantRepository ongParticipantRepository;
    private final OngParticipantRoleRepository ongParticipantRoleRepository;

    public OngService(
            OngMapper ongMapper,
            OngRepository ongRepository,
            UserProfileRepository userProfileRepository,
            OngFollowerRepository ongFollowerRepository,
            OngCategoryRepository ongCategoryRepository,
            OngParticipantRepository ongParticipantRepository, OngParticipantRoleRepository ongParticipantRoleRepository) {
        this.ongMapper = ongMapper;
        this.ongRepository = ongRepository;
        this.userProfileRepository = userProfileRepository;
        this.ongFollowerRepository = ongFollowerRepository;
        this.ongCategoryRepository = ongCategoryRepository;
        this.ongParticipantRepository = ongParticipantRepository;
        this.ongParticipantRoleRepository = ongParticipantRoleRepository;
    }

    public PagedResponse<OngSummaryResponse> getAll(UUID authenticatedUserId, Pageable pageable){
        Page<OngSummary> ongPage = ongRepository.findAllOngSummary(authenticatedUserId, pageable);

        return PagedResponse.<OngSummaryResponse>builder()
                .page(ongPage.getNumber())
                .size(ongPage.getSize())
                .totalPages(ongPage.getTotalPages())
                .totalElements(ongPage.getTotalElements())
                .isLast(ongPage.isLast())
                .content(ongPage.getContent().stream()
                        .map(ong -> ongMapper.toResponse(ong))
                        .toList()
                )
                .build();
    }

    public OngSummaryResponse getById(UUID ongId, UUID authenticatedUserId) {
        return ongMapper.toResponse(
                ongRepository.findOngSummaryById(ongId, authenticatedUserId)
                        .orElseThrow(() -> new OngNotFoundException("ONG not found with ID: " + ongId))
        );
    }

    @Transactional
    public OngSummaryResponse create(CreateOngRequest request, UUID authenticatedUserId) {
        if (ongRepository.existsByName(request.name()))
            throw new OngAlreadyExistsException("An ONG with this name already exists") ;

        UserProfile userOwner = userProfileRepository.getReferenceById(authenticatedUserId);

        Ong ong = registerNewOng(request, userOwner);

        registerOngOwnerParticipant(userOwner, ong);

        return ongMapper.toResponse(
                ongRepository.findOngSummaryById(ong.getId(), authenticatedUserId)
                    .orElseThrow(() -> new OngNotFoundException("ONG not found with ID: " + ong.getId()))
        );
    }

    private Ong registerNewOng(CreateOngRequest request, UserProfile userOwner){
        Ong newOng = new Ong();
        newOng.setName(request.name());
        newOng.setTitle(request.title());
        newOng.setDescription(request.description());
        newOng.setCategory(
                ongCategoryRepository.getReferenceById(
                        OngCategory.Values.getIdByName(request.category())
                )
        );
        newOng.setUserOwner(userOwner);
        newOng.setCreatedAt(Instant.now());

        return ongRepository.save(newOng);
    }

    private void registerOngOwnerParticipant(UserProfile ongOwner, Ong ong){
        OngParticipant userOwnerParticipant = new OngParticipant();
        userOwnerParticipant.setOng(ong);
        userOwnerParticipant.setUser(ongOwner);
        userOwnerParticipant.setUser(ongOwner);
        userOwnerParticipant.setCreatedAt(Instant.now());
        userOwnerParticipant.setRole(
                new OngParticipantRole(OngParticipantRole.Value.OWNER.getId(), OngParticipantRole.Value.OWNER.getName())
        );

        ongParticipantRepository.save(userOwnerParticipant);
    }

    public void delete(UUID ongId, UUID authenticatedUserID) {
        Ong ong = ongRepository.findById(ongId)
                .orElseThrow(() -> new OngNotFoundException());

        if (!ong.getUserOwner().getUserId().equals(authenticatedUserID)) {
            throw new ForbiddenOperationException("Only the owner can delete the ONG");
        }

        ongRepository.delete(ong);
    }

    public void followOng(UUID ongId, UUID authenticatedUserId) {
        Ong ong = ongRepository.findById(ongId)
                .orElseThrow(() -> new OngNotFoundException());

        OngFollowerId followerId = new OngFollowerId(ong, userProfileRepository.getReferenceById(authenticatedUserId));

        if (ongFollowerRepository.existsById(followerId)) {
            throw new UserAlreadyFollowingONG("User is already following this ONG");
        }

        OngFollower follower = new OngFollower();
        follower.setOng(ong);
        follower.setUser(userProfileRepository.getReferenceById(authenticatedUserId));
        follower.setCreatedAt(Instant.now());

        ongFollowerRepository.save(follower);
    }

    public void unfollowOng(UUID ongId, UUID authenticatedUserId) {
        Ong ong = ongRepository.findById(ongId)
                .orElseThrow(() -> new OngNotFoundException());

        OngFollowerId followerId = new OngFollowerId(ong, userProfileRepository.getReferenceById(authenticatedUserId));

        if (!ongFollowerRepository.existsById(followerId)) {
            throw new UserNotFollowingONG("User is not following this ONG");
        }

        ongFollowerRepository.deleteById(followerId);
    }

    public OngResponse update(UUID ongId, @Valid UpdateOngRequest request, UUID userId) {
        Ong ong = ongRepository.findById(ongId)
                .orElseThrow(() -> new OngNotFoundException());

        if (!ong.getUserOwner().getUserId().equals(userId)) {
            throw new ForbiddenOperationException("Only the owner can update the ONG");
        }

        if (request.name().isPresent()) {
            ong.setName(request.name().get());
        }
        if (request.title().isPresent()) {
            ong.setTitle(request.title().get());
        }
        if (request.description().isPresent()) {
            ong.setDescription(request.description().get());
        }

        Ong savedOng = ongRepository.save(ong);

        return ongMapper.toResponse(savedOng);
    }
}
