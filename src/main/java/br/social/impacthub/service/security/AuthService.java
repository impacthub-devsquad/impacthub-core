package br.social.impacthub.service.security;

import br.social.impacthub.exception.InvalidAccessTokenException;
import br.social.impacthub.exception.UserNotAuthenticatedException;
import br.social.impacthub.exception.WrongCredentialsException;
import br.social.impacthub.model.dto.LoginRequest;
import br.social.impacthub.model.dto.LoginResponse;
import br.social.impacthub.model.dto.RefreshRequest;
import br.social.impacthub.model.dto.RegisterUserRequest;
import br.social.impacthub.model.dto.security.AuthenticatedUser;
import br.social.impacthub.model.entity.UserCredentials;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {
    private UserCredentialsService userCredentialsService;
    private TokenService tokenService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    public AuthService(UserCredentialsService userCredentialsService, TokenService tokenService, PasswordEncoder passwordEncoder, @Lazy AuthenticationManager authenticationManager) {
        this.userCredentialsService = userCredentialsService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public void register(@Valid RegisterUserRequest request){
        String encryptedPassword = passwordEncoder.encode(request.password());
        userCredentialsService.create(request.username(), request.email(), encryptedPassword);
    }

    @Transactional
    public LoginResponse login(LoginRequest request){
        Authentication auth = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        try {
            authenticationManager.authenticate(auth);
        } catch (BadCredentialsException e) {
            throw new WrongCredentialsException();
        }

        UUID userId = userCredentialsService.getByEmail(request.email()).userId();
        return tokenService.login(userId);
    }

    @Transactional
    public LoginResponse refresh(RefreshRequest request){
        return tokenService.refresh(request.refreshToken());
    }

    public AuthenticatedUser getAuthenticatedUser(){
        try {
            UserCredentials userCredentials = (UserCredentials) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new AuthenticatedUser(
                    userCredentials.getId(),
                    userCredentials.getUsername(),
                    userCredentials.getEmail()
            );
        } catch (Exception e){
            throw new UserNotAuthenticatedException();
        }
    }

    public void validateAccessToken(String accessToken){
        tokenService.validateAccessToken(accessToken);
    }

    public UUID getUserId(String accessToken){
        try {
            return UUID.fromString(
                    tokenService.getSubjectFromAccessToken(accessToken)
            );
        } catch (RuntimeException e){
            throw new InvalidAccessTokenException();
        }
    }
}
