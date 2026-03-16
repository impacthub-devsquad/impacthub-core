package br.social.impacthub.service;

import br.social.impacthub.model.dto.PagedResponse;
import br.social.impacthub.model.dto.UserProfileResponse;
import br.social.impacthub.model.entity.UserProfile;
import br.social.impacthub.repository.UserProfileRepository;
import br.social.impacthub.service.mapper.UserProfileMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserProfileService {
    private UserProfileRepository userProfileRepository;
    private UserProfileMapper userProfileMapper;

    public UserProfileService(UserProfileRepository userProfileRepository, UserProfileMapper userProfileMapper) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileMapper = userProfileMapper;
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
}
