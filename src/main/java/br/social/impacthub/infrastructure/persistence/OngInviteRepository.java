package br.social.impacthub.infrastructure.persistence;

import br.social.impacthub.model.entity.OngInvite;
import br.social.impacthub.model.entity.OngInviteId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OngInviteRepository extends JpaRepository<OngInvite, OngInviteId> {
}
