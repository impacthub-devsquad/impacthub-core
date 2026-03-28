package br.social.impacthub.infrastructure.persistence;

import br.social.impacthub.model.entity.OngFollower;
import br.social.impacthub.model.entity.OngFollowerId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OngFollowerRepository extends JpaRepository<OngFollower, OngFollowerId> {
}
