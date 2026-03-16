package br.social.impacthub.service.security;

import br.social.impacthub.events.UserCredentialsCreatedEvent;
import br.social.impacthub.exception.UserNotExistsException;
import br.social.impacthub.exception.UsernameOrEmailAlreadyExistsException;
import br.social.impacthub.model.dto.UserCredentialsResponse;
import br.social.impacthub.model.entity.UserCredentials;
import br.social.impacthub.model.entity.UserRole;
import br.social.impacthub.repository.UserCredentialsRepository;
import br.social.impacthub.service.mapper.UserCredentialsMapper;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserCredentialsService implements UserDetailsService {
    private UserCredentialsRepository userCredentialsRepository;
    private UserCredentialsMapper userCredentialsMapper;
    private ApplicationEventPublisher applicationEventPublisher;

    public UserCredentialsService(UserCredentialsRepository userCredentialsRepository, UserCredentialsMapper userCredentialsMapper, ApplicationEventPublisher applicationEventPublisher) {
        this.userCredentialsRepository = userCredentialsRepository;
        this.userCredentialsMapper = userCredentialsMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userCredentialsRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public boolean existsByUsername(String username) {
        return userCredentialsRepository.findByUsername(username).isPresent();
    }

    public boolean existsByEmail(String email) {
        return userCredentialsRepository.findByEmail(email).isPresent();
    }

    public UserCredentialsResponse create(String username, String email, String encryptedPassword) {
        if (userCredentialsRepository.findByUsername(username).isPresent() || userCredentialsRepository.findByEmail(email).isPresent()) {
            throw new UsernameOrEmailAlreadyExistsException();
        }

        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setUsername(username);
        userCredentials.setEmail(email);
        userCredentials.setEncryptedPassword(encryptedPassword);
        userCredentials.setRole(UserRole.USER);

        UserCredentialsResponse response = userCredentialsMapper.toResponse(
                userCredentialsRepository.save(userCredentials)
        );

        applicationEventPublisher.publishEvent(
                new UserCredentialsCreatedEvent(response.userId(), response.username(), email, this)
        );

        return response;
    }

    public UserCredentialsResponse getByEmail(String email){
        return userCredentialsMapper.toResponse(
            userCredentialsRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotExistsException())
        );
    }

    public UserCredentials getById(UUID userId) {
        return userCredentialsRepository.findById(userId)
                .orElseThrow(() -> new UserNotExistsException());
    }
}
