package br.social.impacthub.config.security;

import br.social.impacthub.service.JWTService;
import br.social.impacthub.service.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenConfiguration {
    @Bean
    public TokenService tokenService() {
        return new JWTService();
    }
}
