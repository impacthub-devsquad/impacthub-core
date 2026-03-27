package br.social.impacthub.infrastructure.persistence;

import br.social.impacthub.model.entity.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, UUID> {
    Optional<UserCredentials> findByUsername(String username);

    Optional<UserCredentials> findByEmail(String email);
}
