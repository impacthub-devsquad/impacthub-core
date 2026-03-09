package br.social.impacthub.service;

import br.social.impacthub.model.entity.UserProfile;
import br.social.impacthub.repository.UserProfileRepository;
import br.social.impacthub.service.mapper.UserProfileMapper;
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

    public UserProfileResponse create(UUID userId, String username){
        return userProfileMapper.toResponse(
                userProfileRepository.save(
                        new UserProfile(
                                userId,
                                username
                        )
                )
        );
    }
}
