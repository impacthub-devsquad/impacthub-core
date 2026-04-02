package br.social.impacthub.service;

import br.social.impacthub.exception.ForbiddenOperationException;
import br.social.impacthub.exception.OngNotFoundException;
import br.social.impacthub.infrastructure.persistence.OngFollowerRepository;
import br.social.impacthub.infrastructure.persistence.OngRepository;
import br.social.impacthub.infrastructure.persistence.UserProfileRepository;
import br.social.impacthub.model.dto.OngResponse;
import br.social.impacthub.model.dto.PagedResponse;
import br.social.impacthub.model.dto.UpdateOngRequest;
import br.social.impacthub.model.entity.Ong;
import br.social.impacthub.model.entity.OngFollower;
import br.social.impacthub.model.entity.OngFollowerId;
import br.social.impacthub.service.mapper.OngMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OngService {
    private OngMapper ongMapper;
    private OngRepository ongRepository;
    private UserProfileRepository userProfileRepository;
    private OngFollowerRepository ongFollowerRepository;

    public OngService(OngMapper ongMapper, OngRepository ongRepository, UserProfileRepository userProfileRepository, OngFollowerRepository ongFollowerRepository) {
        this.ongMapper = ongMapper;
        this.ongRepository = ongRepository;
        this.userProfileRepository = userProfileRepository;
        this.ongFollowerRepository = ongFollowerRepository;
    }

    public PagedResponse<OngResponse> getAll(Pageable pageable)
    {
        Page<Ong> ongPage = ongRepository.findAll(pageable);

        return PagedResponse.<OngResponse>builder()
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

    public OngResponse getById(UUID ongId) {
        return ongMapper.toResponse(
                ongRepository.findById(ongId)
                        .orElseThrow(() -> new EntityNotFoundException("ONG não encontrada com ID: " + ongId))
        );
    }

    public OngResponse create(UUID ongId, String name, String title , String description) {
          return ongMapper.toResponse(
                  ongRepository.save(
                          new Ong(
                            ongId,
                            name,
                            title,
                            description,
                            null,
                            null
                          )
                  )
          );
    }

    @Transactional
    public void delete(UUID ongId, UUID authenticatedUserID) {
        Ong ong = ongRepository.findById(ongId)
                .orElseThrow(() -> new OngNotFoundException());

        if (!ong.getUserOwner().getUserId().equals(authenticatedUserID)) {
            throw new ForbiddenOperationException("Only the owner can delete the ONG");
        }

        ongRepository.delete(ong);
    }

    public void followOng(UUID authenticatedUserId, UUID ongId) {
        Ong ong = ongRepository.findById(ongId)
                .orElseThrow(() -> new OngNotFoundException());

        OngFollowerId followerId = new OngFollowerId(ong, userProfileRepository.getReferenceById(authenticatedUserId));

        if (ongFollowerRepository.existsById(followerId)) {
            throw new ForbiddenOperationException("User is already following this ONG");
        }

        OngFollower follower = new OngFollower();
        follower.setOng(ong);
        follower.setUser(userProfileRepository.getReferenceById(authenticatedUserId));

        ongFollowerRepository.save(follower);
    }

    public void unfollowOng(UUID authenticatedUserId, UUID ongId) {
        Ong ong = ongRepository.findById(ongId)
                .orElseThrow(() -> new OngNotFoundException());

        OngFollowerId followerId = new OngFollowerId(ong, userProfileRepository.getReferenceById(authenticatedUserId));

        if (!ongFollowerRepository.existsById(followerId)) {
            throw new ForbiddenOperationException("User is not following this ONG");
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

        return ongMapper.toResponse(ongRepository.save(ong));
    }
}
