package br.social.impacthub.infrastructure.persistence;

import br.social.impacthub.model.dto.EventSummary;
import br.social.impacthub.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    @Query(
        """
        SELECT
            e.id as id,
            e.title as title,
            e.description as description,
            e.ong.id as ongId,
            e.createdAt as createdAt,
            e.createdBy.userId as createdBy,
            (
                SELECT COUNT(l)
                FROM EventLike l
                WHERE l.event.id = :eventId
            ) as likesCount
        FROM Event e
        WHERE e.id = :eventId
        """
    )
    Optional<EventSummary> getEventSummary(@Param("eventId") UUID eventId);

    @Query(
            """
            SELECT
                e.id as id,
                e.title as title,
                e.description as description,
                e.ong.id as ongId,
                e.createdAt as createdAt,
                e.createdBy.userId as createdBy,
                (
                    SELECT COUNT(l)
                    FROM EventLike l
                    WHERE l.event.id = e.id
                ) as likesCount
            FROM Event e
            """
    )
    Optional<EventSummary> getAllEventSummary(UUID eventId);
}
