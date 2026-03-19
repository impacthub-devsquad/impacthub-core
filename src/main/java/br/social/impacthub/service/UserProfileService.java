package br.social.impacthub.service;

import br.social.impacthub.events.UserProfileDeletedEvent;
import br.social.impacthub.events.UserProfileUpdatedEvent;
import br.social.impacthub.exception.UserNotExistsException;
import br.social.impacthub.model.dto.PagedResponse;
import br.social.impacthub.model.dto.UpdateUserRequest;
import br.social.impacthub.model.dto.UserProfileResponse;
import br.social.impacthub.model.entity.UserProfile;
import br.social.impacthub.repository.UserProfileRepository;
import br.social.impacthub.service.mapper.UserProfileMapper;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserProfileService {
    private UserProfileRepository userProfileRepository;
    private UserProfileMapper userProfileMapper;
    private ApplicationEventPublisher applicationEventPublisher;

    public UserProfileService(UserProfileRepository userProfileRepository, UserProfileMapper userProfileMapper, ApplicationEventPublisher applicationEventPublisher) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileMapper = userProfileMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public PagedResponse<UserProfileResponse> getAll(Pageable pageable){
        Page<UserProfile> userPage = userProfileRepository.findAll(pageable);

        return PagedResponse.<UserProfileResponse>builder()
                .page(userPage.getNumber())
                .size(userPage.getSize())
                .totalPages(userPage.getTotalPages())
                .totalElements(userPage.getTotalElements())
                .isLast(userPage.isLast())
                .content(userPage.getContent().stream()
                        .map(userProfile ->
                               userProfileMapper.toResponse(userProfile)
                        )
                        .toList()
                )
                .build();
    }

    public UserProfileResponse create(UUID userId, String username, String email){
        return userProfileMapper.toResponse(
                userProfileRepository.save(
                        new UserProfile(
                                userId,
                                username,
                                email,
                                ""
                        )
                )
        );
    }

    public PagedResponse<UserProfileResponse> search(String query, Pageable pageable) {
        Page<UserProfile> userPage = userProfileRepository.queryByUsername(query, pageable);

        return PagedResponse.<UserProfileResponse>builder()
                .page(userPage.getNumber())
                .size(userPage.getSize())
                .totalPages(userPage.getTotalPages())
                .totalElements(userPage.getTotalElements())
                .isLast(userPage.isLast())
                .content(userPage.getContent().stream()
                        .map(userProfile ->
                                userProfileMapper.toResponse(userProfile)
                        )
                        .toList()
                )
                .build();
    }

    public UserProfileResponse getById(UUID id) {
        return userProfileMapper.toResponse(
                userProfileRepository.findById(id)
                    .orElseThrow(() -> new UserNotExistsException())
        );
    }

    @Transactional
    public void delete(UUID authenticatedUserId) {
        userProfileRepository.deleteById(authenticatedUserId);
        applicationEventPublisher.publishEvent(new UserProfileDeletedEvent(authenticatedUserId, this));
    }

    @Transactional
    public UserProfileResponse patch(UUID userId, UpdateUserRequest request) {
        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new UserNotExistsException());

        if (request.username().isPresent()){
            userProfile.setUsername(request.username().get());
        }

        if (request.description().isPresent()){
            userProfile.setDescription(request.description().get());
        }

        applicationEventPublisher.publishEvent(new UserProfileUpdatedEvent(userProfile, this));

        return userProfileMapper.toResponse(
                userProfileRepository.save(userProfile)
        );
    }
}
