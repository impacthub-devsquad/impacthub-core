package br.social.impacthub.infrastructure.persistence;

import br.social.impacthub.model.entity.OngInvite;
import br.social.impacthub.model.entity.OngInviteId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OngInviteRepository extends JpaRepository<OngInvite, OngInviteId> {
    Page<OngInvite> findAllByOng_OngId(UUID ongId, Pageable pageable);
}
