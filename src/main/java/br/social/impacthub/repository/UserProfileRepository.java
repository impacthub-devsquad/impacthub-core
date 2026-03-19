package br.social.impacthub.repository;

import br.social.impacthub.model.entity.UserCredentials;
import br.social.impacthub.model.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    List<UserProfile> findByUsername(String username);

    @Query(value =
        """
        SELECT u FROM UserProfile u
        WHERE u.username ILIKE CONCAT(:query, '%')
        """
    )
    Page<UserProfile> queryByUsername(@Param(value = "query") String query, Pageable pageable);
}
