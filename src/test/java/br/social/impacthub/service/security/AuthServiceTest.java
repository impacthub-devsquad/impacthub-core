package br.social.impacthub.service.security;

import br.social.impacthub.exception.InvalidAccessTokenException;
import br.social.impacthub.exception.InvalidEmailAddressException;
import br.social.impacthub.exception.UserNotAuthenticatedException;
import br.social.impacthub.exception.WrongCredentialsException;
import br.social.impacthub.infrastructure.web.EmailValidatorClient;
import br.social.impacthub.model.dto.EmailValidatorResponse;
import br.social.impacthub.model.dto.LoginRequest;
import br.social.impacthub.model.dto.LoginResponse;
import br.social.impacthub.model.dto.RefreshRequest;
import br.social.impacthub.model.dto.RegisterUserRequest;
import br.social.impacthub.model.dto.UserCredentialsResponse;
import br.social.impacthub.model.dto.security.AuthenticatedUser;
import br.social.impacthub.model.entity.UserCredentials;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
public class AuthServiceTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private UserCredentialsService userCredentialsService;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private EmailValidatorClient emailValidatorClient;

    private AutoCloseable mock;

    @BeforeEach
    void setUp(){
        mock = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }


    @Test
    @DisplayName("Should register a new user if everything is OK")
    public void registerTestCase1(){
//        Given
        RegisterUserRequest request = new RegisterUserRequest("test12345", "email@gmail.com", "12345678");
        String encryptedPassword = "valid";

        Mockito.when(emailValidatorClient.validate(request.email()))
                .thenReturn(new EmailValidatorResponse(100, encryptedPassword));

        Mockito.when(passwordEncoder.encode(request.password()))
                .thenReturn(encryptedPassword);

//        When
        authService.register(request);

//        Then
        Mockito.verify(userCredentialsService, Mockito.times(1))
                .create(request.username(),  request.email(), encryptedPassword);
    }

    @Test
    @DisplayName("Should not register a new user if email validation fails")
    public void registerTestCase2(){
        // Given
        RegisterUserRequest request = new RegisterUserRequest("test12345", "invalid@email.com", "12345678");
        
        Mockito.when(emailValidatorClient.validate(request.email()))
                .thenReturn(new EmailValidatorResponse(50, "invalid"));
        
        // When & Then
        assertThrows(InvalidEmailAddressException.class, () -> {
            authService.register(request);
        });
        
        Mockito.verify(userCredentialsService, Mockito.never())
                .create(any(), any(), any());
    }

    @Test
    @DisplayName("Should call EmailClient to validate Email")
    public void validateEmailWithClientTestCase1(){
//        Given
        String email = "test@email.com";
        EmailValidatorResponse response = new EmailValidatorResponse(100, "valid");
        
        Mockito.when(emailValidatorClient.validate(email))
                .thenReturn(response);
        
//        When
        authService.register(new RegisterUserRequest("testUser", email, "12345678"));
        
//        Then
        Mockito.verify(emailValidatorClient, Mockito.times(1))
                .validate(email);
    }

    @Test
    @DisplayName("Should login if credentials are correct")
    public void loginTestCase1(){
        // Given
        LoginRequest request = new LoginRequest("email@test.com", "password123");
        UUID expectedUserId = UUID.randomUUID();
        UserCredentialsResponse userCredentialsResponse = new UserCredentialsResponse(
                expectedUserId,
                "testuser",
                request.email()
        );
        LoginResponse loginResponse = new LoginResponse("accessToken", "refreshToken");
        
        Mockito.when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        
        Mockito.when(userCredentialsService.getByEmail(request.email()))
                .thenReturn(userCredentialsResponse);
        
        Mockito.when(tokenService.login(expectedUserId))
                .thenReturn(loginResponse);
        
        // When
        LoginResponse result = authService.login(request);
        
        // Then
        assertNotNull(result);
        assertEquals(loginResponse, result);
        Mockito.verify(authenticationManager, Mockito.times(1))
                .authenticate(any());
        Mockito.verify(tokenService, Mockito.times(1))
                .login(expectedUserId);
    }

    @Test
    @DisplayName("Shouldn't login if credentials are incorrect")
    public void loginTestCase2(){
        // Given
        LoginRequest request = new LoginRequest("email@test.com", "wrongpassword");
        
        Mockito.when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));
        
        // When & Then
        assertThrows(WrongCredentialsException.class, () -> {
            authService.login(request);
        });
        
        Mockito.verify(authenticationManager, Mockito.times(1))
                .authenticate(any());
        Mockito.verify(tokenService, Mockito.never())
                .login(any());
    }

    @Test
    @DisplayName("Should refresh tokens if refresh token is valid")
    public void refreshTestCase1(){
        // Given
        String validRefreshToken = "valid_refresh_token";
        RefreshRequest request = new RefreshRequest(validRefreshToken);
        LoginResponse refreshedResponse = new LoginResponse("newAccessToken", "newRefreshToken");
        
        Mockito.when(tokenService.refresh(validRefreshToken))
                .thenReturn(refreshedResponse);
        
        // When
        var result = authService.refresh(request);
        
        // Then
        assertNotNull(result);
        assertEquals(refreshedResponse, result);
        Mockito.verify(tokenService, Mockito.times(1))
                .refresh(validRefreshToken);
    }

    @Test
    @DisplayName("Shouldn't refresh tokens if refresh token is invalid")
    public void refreshTestCase2(){
        // Given
        String invalidRefreshToken = "invalid_refresh_token";
        RefreshRequest request = new RefreshRequest(invalidRefreshToken);
        
        Mockito.when(tokenService.refresh(invalidRefreshToken))
                .thenThrow(new RuntimeException("Invalid refresh token"));
        
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            authService.refresh(request);
        });
        
        Mockito.verify(tokenService, Mockito.times(1))
                .refresh(invalidRefreshToken);
    }

    @Test
    @DisplayName("Should get authenticated user from SecurityContext successfully")
    public void getAuthenticatedUserTestCase1(){
        UUID userId = UUID.randomUUID();
        UserCredentials userCredentials = new UserCredentials(
                userId, 
                "testuser", 
                "test@email.com", 
                "encrypted",
                null,
                null
        );

        try(MockedStatic<SecurityContextHolder> mock = Mockito.mockStatic(SecurityContextHolder.class)) {
            SecurityContext context = Mockito.mock(SecurityContext.class);
            Authentication auth = Mockito.mock(Authentication.class);

            mock.when(SecurityContextHolder::getContext)
                    .thenReturn(context);

            Mockito.when(context.getAuthentication())
                    .thenReturn(auth);

            Mockito.when(auth.getPrincipal())
                    .thenReturn(userCredentials);

            AuthenticatedUser result = authService.getAuthenticatedUser();

            assertNotNull(result);
            assertEquals(userId, result.userId());
            assertEquals("testuser", result.username());
            assertEquals("test@email.com", result.email());
        }
    }

    @Test
    @DisplayName("Should call TokenService to validate access token")
    public void validateAccessTokenTestCase1(){
        // Given
        String accessToken = "valid_access_token";
        
        // When
        authService.validateAccessToken(accessToken);
        
        // Then
        Mockito.verify(tokenService, Mockito.times(1))
                .validateAccessToken(accessToken);
    }

    @Test
    @DisplayName("Should return the userId from accessToken String successfully")
    public void getUserIdTestCase1(){
        // Given
        String accessToken = "valid_access_token";
        UUID expectedUserId = UUID.randomUUID();
        String userIdString = expectedUserId.toString();
        
        Mockito.when(tokenService.getSubjectFromAccessToken(accessToken))
                .thenReturn(userIdString);
        
        // When
        var result = authService.getUserId(accessToken);
        
        // Then
        assertNotNull(result);
        assertEquals(expectedUserId, result);
        Mockito.verify(tokenService, Mockito.times(1))
                .getSubjectFromAccessToken(accessToken);
    }
}