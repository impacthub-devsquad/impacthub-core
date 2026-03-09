package br.social.impacthub.service.mapper;

import br.social.impacthub.model.dto.UserCredentialsResponse;
import br.social.impacthub.model.entity.UserCredentials;
import org.springframework.stereotype.Component;

@Component
public class UserCredentialsMapper {
    public UserCredentialsResponse toResponse(UserCredentials userCredentials) {
        return new UserCredentialsResponse(
                userCredentials.getId(),
                userCredentials.getUsername(),
                userCredentials.getEmail()
        );
    }
}
