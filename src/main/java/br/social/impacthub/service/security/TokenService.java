package br.social.impacthub.service.security;

import br.social.impacthub.model.dto.LoginResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface TokenService {
    void validateAccessToken(String accessToken);

    void validateRefreshToken(String refreshToken);

    LoginResponse login(UUID userId);

    LoginResponse refresh(String refreshToken);

    String getSubjectFromAccessToken(String accessToken);
}
