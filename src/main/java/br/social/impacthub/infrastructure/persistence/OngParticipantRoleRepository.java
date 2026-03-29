package br.social.impacthub.infrastructure.persistence;

import br.social.impacthub.model.entity.OngInvite;
import br.social.impacthub.model.entity.OngInviteId;
import br.social.impacthub.model.entity.OngParticipantRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OngParticipantRoleRepository extends JpaRepository<OngParticipantRole, Integer> {
    OngParticipantRole getReferenceByName(String name);
}
