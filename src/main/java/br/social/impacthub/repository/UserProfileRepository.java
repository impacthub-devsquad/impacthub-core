package br.social.impacthub.repository;

import br.social.impacthub.model.entity.UserCredentials;
import br.social.impacthub.model.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    List<UserProfile> findByUsername(String username);
}
