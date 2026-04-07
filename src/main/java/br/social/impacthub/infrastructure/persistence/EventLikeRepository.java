package br.social.impacthub.infrastructure.persistence;

import br.social.impacthub.model.entity.EventLike;
import br.social.impacthub.model.entity.EventLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventLikeRepository extends JpaRepository<EventLike, EventLikeId> {
}
