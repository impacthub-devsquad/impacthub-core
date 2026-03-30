package br.social.impacthub.infrastructure.persistence;

import br.social.impacthub.model.entity.OngParticipant;
import br.social.impacthub.model.entity.OngParticipantId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface OngParticipantRepository extends JpaRepository<OngParticipant, OngParticipantId> {
    Page<OngParticipant> findAllByOngId(UUID ongId, Pageable pageable);
}
