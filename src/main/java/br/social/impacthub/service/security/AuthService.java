package br.social.impacthub.service.security;

import br.social.impacthub.exception.InvalidAccessTokenException;
import br.social.impacthub.exception.InvalidEmailAddressException;
import br.social.impacthub.exception.UserNotAuthenticatedException;
import br.social.impacthub.exception.WrongCredentialsException;
import br.social.impacthub.infrastructure.web.EmailValidatorClient;
import br.social.impacthub.model.dto.*;
import br.social.impacthub.model.dto.security.AuthenticatedUser;
import br.social.impacthub.model.entity.UserCredentials;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
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
    private EmailValidatorClient emailValidatorClient;

    public AuthService(
            UserCredentialsService userCredentialsService,
            TokenService tokenService,
            PasswordEncoder passwordEncoder,
            @Lazy AuthenticationManager authenticationManager,
            EmailValidatorClient emailValidatorClient
    ) {
        this.userCredentialsService = userCredentialsService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailValidatorClient = emailValidatorClient;
    }

    @Transactional
    public void register(@Valid RegisterUserRequest request){
        validateEmailWithClient(request.email());

        String encryptedPassword = passwordEncoder.encode(request.password());
        userCredentialsService.create(request.username(), request.email(), encryptedPassword);
    }

    private void validateEmailWithClient(String email){
        EmailValidatorResponse response = null;
        try {
            response = emailValidatorClient.validate(email);
        }
        catch (Exception e){
            throw new RuntimeException("Email validation failed");
        }
        finally {
            if (response != null && response.score() < 100)
                throw new InvalidEmailAddressException("Invalid email address");
        }
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
