package br.social.impacthub.service;

import org.springframework.stereotype.Service;

@Service
public interface TokenService {
    String validateTokenAndRetrieveUsername(String token);

    String generateToken(String username);
}
