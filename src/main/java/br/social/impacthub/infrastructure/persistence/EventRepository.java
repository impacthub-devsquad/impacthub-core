package br.social.impacthub.infrastructure.persistence;

import br.social.impacthub.model.dto.EventSummary;
import br.social.impacthub.model.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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
            e.createdBy as createdBy,
            e.viewsCount as viewsCount,
            (
                SELECT COUNT(l)
                FROM EventLike l
                WHERE l.event.id = :eventId
            ) as likesCount,
            (
                SELECT CASE
                    WHEN COUNT(l) > 0 THEN true
                    ELSE false
                END
                FROM EventLike l
                WHERE l.event.id = :eventId AND l.user.userId = :userId
            ) as isLiked
        FROM Event e
        WHERE e.id = :eventId
        """
    )
    Optional<EventSummary> getEventSummaryById(
            @Param("eventId") UUID eventId,
            @Param("userId") UUID authenticatedUserId
    );

    @Query(
        """
        SELECT
            e.id as id,
            e.title as title,
            e.description as description,
            e.ong.id as ongId,
            e.createdAt as createdAt,
            e.createdBy as createdBy,
            e.viewsCount as viewsCount,
            (
                SELECT COUNT(l)
                FROM EventLike l
                WHERE l.event.id = e.id
            ) as likesCount,
            (
                SELECT CASE
                    WHEN COUNT(l) > 0 THEN true
                    ELSE false
                END
                FROM EventLike l
                WHERE l.event.id = e.id AND l.user.userId = :userId
            ) as isLiked
        FROM Event e
        """
    )
    Page<EventSummary> getAllEventSummary(
            @Param("userId") UUID authenticatedUserId,
            Pageable pageable
    );

    @Query(
            """
            SELECT
                e.id as id,
                e.title as title,
                e.description as description,
                e.ong.id as ongId,
                e.createdAt as createdAt,
                e.createdBy as createdBy,
                e.viewsCount as viewsCount,
                (
                    SELECT COUNT(l)
                    FROM EventLike l
                    WHERE l.event.id = e.id
                ) as likesCount,
                (
                    SELECT CASE
                        WHEN COUNT(l) > 0 THEN true
                        ELSE false
                    END
                    FROM EventLike l
                    WHERE l.event.id = e.id AND l.user.userId = :userId
                ) as isLiked
            FROM Event e
            WHERE e.ong.id = :ongId
            """
    )
    Page<EventSummary> getAllEventSummaryByOngId(
            @Param("ongId") UUID ongID,
            @Param("userId") UUID authenticatedUserId,
            Pageable pageable
    );
}
