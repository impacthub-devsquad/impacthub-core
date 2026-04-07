package br.social.impacthub.infrastructure.persistence;

import br.social.impacthub.model.entity.OngParticipant;
import br.social.impacthub.model.entity.OngParticipantId;
import br.social.impacthub.model.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OngParticipantRepository extends JpaRepository<OngParticipant, OngParticipantId> {
    Page<OngParticipant> findAllByOng_Id(UUID ongId, Pageable pageable);

    UserProfile user(UserProfile user);
}
