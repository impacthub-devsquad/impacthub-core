package br.social.impacthub.infrastructure.persistence;

import br.social.impacthub.model.entity.OngParticipant;
import br.social.impacthub.model.entity.OngParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OngParticipantRepository extends JpaRepository<OngParticipant, OngParticipantId> {
}
