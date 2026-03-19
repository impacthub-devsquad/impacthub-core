package br.social.impacthub.service.mapper;

import br.social.impacthub.model.dto.security.AuthenticatedUser;
import br.social.impacthub.model.entity.UserProfile;
import br.social.impacthub.model.dto.UserProfileResponse;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapper {
    public UserProfileResponse toResponse(UserProfile userProfile) {
        return new UserProfileResponse(
                userProfile.getUserId(),
                userProfile.getUsername(),
                userProfile.getEmail()
        );
    }

    public UserProfileResponse toResponse(AuthenticatedUser authenticatedUser) {
        return new UserProfileResponse(
                authenticatedUser.userId(),
                authenticatedUser.username(),
                authenticatedUser.email()
        );
    }
}
