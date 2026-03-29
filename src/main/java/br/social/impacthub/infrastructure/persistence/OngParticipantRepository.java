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
    Page<OngParticipant> findAllByOngId(
            @Param("ongId") UUID ongId,
            Pageable pageable
    );

    @Query("""
        SELECT p FROM OngParticipant p
        WHERE p.ong.id = :ongId AND p.user.userId = :userId
    """)
    Optional<OngParticipant> findByOngIdAndUserId(
            @Param("ongId") UUID ongId,
            @Param("userId") UUID userId
    );

    @Transactional
    @Modifying
    @Query("""
        DELETE FROM OngParticipant p
        WHERE p.ong.id = :ongId AND p.user.userId = :userId
    """)
    void deleteByOngIdAndUserId(
            @Param("ongId") UUID ongId,
            @Param("userId") UUID userId
    );
}
