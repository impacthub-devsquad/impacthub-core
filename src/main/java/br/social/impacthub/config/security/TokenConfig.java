package br.social.impacthub.config.security;

import br.social.impacthub.repository.RefreshTokenRepository;
import br.social.impacthub.service.security.JWTService;
import br.social.impacthub.service.security.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenConfig {
    @Bean
    public TokenService tokenService(RefreshTokenRepository refreshTokenRepository) {
        return new JWTService(refreshTokenRepository);
    }
}
