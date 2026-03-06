package br.social.impacthub.repository;

import br.social.impacthub.model.entity.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, UUID> {
}
