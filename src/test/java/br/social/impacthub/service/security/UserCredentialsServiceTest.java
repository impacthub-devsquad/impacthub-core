package br.social.impacthub.service.security;

import br.social.impacthub.events.UserCredentialsCreatedEvent;
import br.social.impacthub.exception.UserNotExistsException;
import br.social.impacthub.exception.UsernameOrEmailAlreadyExistsException;
import br.social.impacthub.infrastructure.persistence.UserCredentialsRepository;
import br.social.impacthub.model.dto.UserCredentialsResponse;
import br.social.impacthub.model.entity.UserCredentials;
import br.social.impacthub.model.entity.UserProfile;
import br.social.impacthub.model.entity.UserRole;
import br.social.impacthub.service.mapper.UserCredentialsMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
public class UserCredentialsServiceTest {
    @Mock
    private UserCredentialsRepository userCredentialsRepository;

    @Mock
    private UserCredentialsMapper userCredentialsMapper;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private UserCredentialsService userCredentialsService;

    private AutoCloseable mock;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    @DisplayName("Should load user by Username successfully if exists")
    void loadUserByUsernameTestCase1() {
//        Given
        String email = "email@gmail.com";
        UserCredentials userCredentials = new UserCredentials(
                UUID.randomUUID(),
                "username",
                email,
                "encrypted_password",
                UserRole.USER,
                Instant.now()
        );
        UserCredentialsResponse expectedResponse = new UserCredentialsResponse(
                userCredentials.getId(),
                userCredentials.getUsername(),
                userCredentials.getEmail()
        );

        Mockito.when(userCredentialsRepository.findByEmail(email))
                .thenReturn(Optional.of(userCredentials));

        Mockito.when(userCredentialsMapper.toResponse(userCredentials))
                .thenReturn(expectedResponse);

//        When
        UserCredentials response = (UserCredentials) userCredentialsService.loadUserByUsername(email);

//        Then
        Mockito.verify(userCredentialsRepository, Mockito.times(1))
                .findByEmail(email);

        assertEquals(expectedResponse.email(), response.getEmail());
        assertEquals(expectedResponse.username(), response.getUsername());
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException if user not found by username")
    void loadUserByUsernameTestCase2() {
        // Given
        String email = "notfound@gmail.com";
        
        Mockito.when(userCredentialsRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(UsernameNotFoundException.class, () -> {
            userCredentialsService.loadUserByUsername(email);
        });
        
        Mockito.verify(userCredentialsRepository, Mockito.times(1))
                .findByEmail(email);
    }

    @Test
    @DisplayName("Should return true if user exists by username")
    void existsByUsernameTestCase1() {
        // Given
        String username = "testuser";
        UserCredentials userCredentials = new UserCredentials(
                UUID.randomUUID(),
                username,
                "email@gmail.com",
                "encrypted_password",
                UserRole.USER,
                Instant.now()
        );
        
        Mockito.when(userCredentialsRepository.findByUsername(username))
                .thenReturn(Optional.of(userCredentials));
        
        // When
        boolean result = userCredentialsService.existsByUsername(username);
        
        // Then
        assertTrue(result);
        Mockito.verify(userCredentialsRepository, Mockito.times(1))
                .findByUsername(username);
    }

    @Test
    @DisplayName("Should return false if user not exists by username")
    void existsByUsernameTestCase2() {
        // Given
        String username = "notexist";
        
        Mockito.when(userCredentialsRepository.findByUsername(any()))
                .thenReturn(Optional.empty());
        
        // When
        boolean result = userCredentialsService.existsByUsername(username);
        
        // Then
        assertFalse(result);
        Mockito.verify(userCredentialsRepository, Mockito.times(1))
                .findByUsername(username);
    }

    @Test
    @DisplayName("Should return true if user exists by email")
    void existsByEmailTestCase1() {
        // Given
        String email = "test@gmail.com";
        UserCredentials userCredentials = new UserCredentials(
                UUID.randomUUID(),
                "testuser",
                email,
                "encrypted_password",
                UserRole.USER,
                Instant.now()
        );
        
        Mockito.when(userCredentialsRepository.findByEmail(email))
                .thenReturn(Optional.of(userCredentials));
        
        // When
        boolean result = userCredentialsService.existsByEmail(email);
        
        // Then
        assertTrue(result);
        Mockito.verify(userCredentialsRepository, Mockito.times(1))
                .findByEmail(email);
    }

    @Test
    @DisplayName("Should return false if user not exists by email")
    void existsByEmailTestCase2() {
        // Given
        String email = "notfound@gmail.com";
        
        Mockito.when(userCredentialsRepository.findByEmail(email))
                .thenReturn(Optional.empty());
        
        // When
        boolean result = userCredentialsService.existsByEmail(email);
        
        // Then
        assertFalse(result);
        Mockito.verify(userCredentialsRepository, Mockito.times(1))
                .findByEmail(email);
    }

    @Test
    @DisplayName("Should create user credentials successfully if credentials are valid")
    void createTestCase1() {
        // Given
        String username = "newuser";
        String email = "newuser@gmail.com";
        String encryptedPassword = "encrypted_pass";
        
        UUID userId = UUID.randomUUID();
        UserCredentials userCredentials = new UserCredentials(
                userId,
                username,
                email,
                encryptedPassword,
                UserRole.USER,
                Instant.now()
        );
        UserCredentialsResponse expectedResponse = new UserCredentialsResponse(
                userId,
                username,
                email
        );
        
        Mockito.when(userCredentialsRepository.findByUsername(username))
                .thenReturn(Optional.empty());
        Mockito.when(userCredentialsRepository.findByEmail(email))
                .thenReturn(Optional.empty());
        Mockito.when(userCredentialsRepository.save(any(UserCredentials.class)))
                .thenReturn(userCredentials);
        Mockito.when(userCredentialsMapper.toResponse(userCredentials))
                .thenReturn(expectedResponse);
        
        // When
        UserCredentialsResponse result = userCredentialsService.create(username, email, encryptedPassword);
        
        // Then
        assertNotNull(result);
        assertEquals(userId, result.userId());
        assertEquals(username, result.username());
        assertEquals(email, result.email());
        Mockito.verify(userCredentialsRepository, Mockito.times(1))
                .findByUsername(username);
        Mockito.verify(userCredentialsRepository, Mockito.times(1))
                .findByEmail(email);
        Mockito.verify(userCredentialsRepository, Mockito.times(1))
                .save(any(UserCredentials.class));
        Mockito.verify(applicationEventPublisher, Mockito.times(1))
                .publishEvent(any(UserCredentialsCreatedEvent.class));
    }

    @Test
    @DisplayName("Should throw UsernameOrEmailAlreadyExistsException if username already exists")
    void createTestCase2() {
        // Given
        String username = "existinguser";
        String email = "newuser@gmail.com";
        String encryptedPassword = "encrypted_pass";
        
        UserCredentials existingUser = new UserCredentials(
                UUID.randomUUID(),
                username,
                "existing@gmail.com",
                "encrypted_password",
                UserRole.USER,
                Instant.now()
        );
        
        Mockito.when(userCredentialsRepository.findByUsername(username))
                .thenReturn(Optional.of(existingUser));
        
        // When & Then
        assertThrows(UsernameOrEmailAlreadyExistsException.class, () -> {
            userCredentialsService.create(username, email, encryptedPassword);
        });
        
        Mockito.verify(userCredentialsRepository, Mockito.times(1))
                .findByUsername(username);
        Mockito.verify(userCredentialsRepository, Mockito.never())
                .save(any());
    }

    @Test
    @DisplayName("Should retrieve user successfully if exists by email")
    void getByEmailTestCase1() {
        // Given
        String email = "test@gmail.com";
        UUID userId = UUID.randomUUID();
        UserCredentials userCredentials = new UserCredentials(
                userId,
                "testuser",
                email,
                "encrypted_password",
                UserRole.USER,
                Instant.now()
        );
        UserCredentialsResponse expectedResponse = new UserCredentialsResponse(
                userId,
                "testuser",
                email
        );
        
        Mockito.when(userCredentialsRepository.findByEmail(email))
                .thenReturn(Optional.of(userCredentials));
        Mockito.when(userCredentialsMapper.toResponse(userCredentials))
                .thenReturn(expectedResponse);
        
        // When
        UserCredentialsResponse result = userCredentialsService.getByEmail(email);
        
        // Then
        assertNotNull(result);
        assertEquals(userId, result.userId());
        assertEquals("testuser", result.username());
        assertEquals(email, result.email());
        Mockito.verify(userCredentialsRepository, Mockito.times(1))
                .findByEmail(email);
        Mockito.verify(userCredentialsMapper, Mockito.times(1))
                .toResponse(userCredentials);
    }

    @Test
    @DisplayName("Should throw UserNotExistsException if user not exists by email")
    void getByEmailTestCase2() {
        // Given
        String email = "notfound@gmail.com";
        
        Mockito.when(userCredentialsRepository.findByEmail(email))
                .thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(UserNotExistsException.class, () -> {
            userCredentialsService.getByEmail(email);
        });
        
        Mockito.verify(userCredentialsRepository, Mockito.times(1))
                .findByEmail(email);
    }

    @Test
    @DisplayName("Should retrieve user successfully if exists by id")
    void getByIdTestCase1() {
        // Given
        UUID userId = UUID.randomUUID();
        UserCredentials userCredentials = new UserCredentials(
                userId,
                "testuser",
                "test@gmail.com",
                "encrypted_password",
                UserRole.USER,
                Instant.now()
        );
        
        Mockito.when(userCredentialsRepository.findById(userId))
                .thenReturn(Optional.of(userCredentials));
        
        // When
        UserCredentials result = userCredentialsService.getById(userId);
        
        // Then
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("testuser", result.getUsername());
        assertEquals("test@gmail.com", result.getEmail());
        Mockito.verify(userCredentialsRepository, Mockito.times(1))
                .findById(userId);
    }

    @Test
    @DisplayName("Should throw UserNotExistsException if user not exists by id")
    void getByIdTestCase2() {
        // Given
        UUID userId = UUID.randomUUID();
        
        Mockito.when(userCredentialsRepository.findById(userId))
                .thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(UserNotExistsException.class, () -> {
            userCredentialsService.getById(userId);
        });
        
        Mockito.verify(userCredentialsRepository, Mockito.times(1))
                .findById(userId);
    }

    @Test
    @DisplayName("Should delete user successfully if exists")
    void deleteByIdTestCase1() {
        // Given
        UUID userId = UUID.randomUUID();
        
        Mockito.when(userCredentialsRepository.existsById(userId))
                .thenReturn(true);
        
        // When
        userCredentialsService.deleteById(userId);
        
        // Then
        Mockito.verify(userCredentialsRepository, Mockito.times(1))
                .existsById(userId);
        Mockito.verify(userCredentialsRepository, Mockito.times(1))
                .deleteById(userId);
    }

    @Test
    @DisplayName("Should throw UserNotExistsException if user not exists when deleting")
    void deleteByIdTestCase2() {
        // Given
        UUID userId = UUID.randomUUID();
        
        Mockito.when(userCredentialsRepository.existsById(userId))
                .thenReturn(false);
        
        // When & Then
        assertThrows(UserNotExistsException.class, () -> {
            userCredentialsService.deleteById(userId);
        });
        
        Mockito.verify(userCredentialsRepository, Mockito.times(1))
                .existsById(userId);
        Mockito.verify(userCredentialsRepository, Mockito.never())
                .deleteById(userId);
    }

    @Test
    @DisplayName("Should update user successfully if exists")
    void updateProfileTestCase1() {
        // Given
        UUID userId = UUID.randomUUID();
        String newUsername = "updateduser";
        
        UserCredentials existingUser = new UserCredentials(
                userId,
                "oldusername",
                "test@gmail.com",
                "encrypted_password",
                UserRole.USER,
                Instant.now()
        );
        
        UserProfile userProfile = new UserProfile(
                userId,
                newUsername,
                "test@gmail.com",
                "description",
                Instant.now()
        );
        
        Mockito.when(userCredentialsRepository.findById(userId))
                .thenReturn(Optional.of(existingUser));
        
        // When
        userCredentialsService.updateProfile(userProfile);
        
        // Then
        Mockito.verify(userCredentialsRepository, Mockito.times(1))
                .findById(userId);
        Mockito.verify(userCredentialsRepository, Mockito.times(1))
                .save(existingUser);
        assertEquals(newUsername, existingUser.getUsername());
    }

    @Test
    @DisplayName("Should throw UserNotExistsException if user not exists when updating profile")
    void updateProfileTestCase2() {
        // Given
        UUID userId = UUID.randomUUID();
        
        UserProfile userProfile = new UserProfile(
                userId,
                "username",
                "test@gmail.com",
                "description",
                Instant.now()
        );
        
        Mockito.when(userCredentialsRepository.findById(userId))
                .thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(UserNotExistsException.class, () -> {
            userCredentialsService.updateProfile(userProfile);
        });
        
        Mockito.verify(userCredentialsRepository, Mockito.times(1))
                .findById(userId);
        Mockito.verify(userCredentialsRepository, Mockito.never())
                .save(any());
    }
}