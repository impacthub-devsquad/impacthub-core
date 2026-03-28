package br.social.impacthub.infrastructure.persistence;

import br.social.impacthub.model.dto.OngResponse;
import br.social.impacthub.model.dto.OngSummary;
import br.social.impacthub.model.entity.Ong;
import br.social.impacthub.model.entity.OngParticipant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface OngRepository extends JpaRepository<Ong, UUID> {
    @Query(value =
    """
    SELECT
        o.id as id,
        o.userOwner.userId as userOwnerId,
        o.name as name,
        o.description as description,
        (
            SELECT COUNT(p)
            FROM OngParticipant p
            WHERE p.ong.id = :ongId
        ) as participantsCount,
        (
            SELECT COUNT(f)  
            FROM OngFollower f
            WHERE f.ong.id = :ongId
        ) as followersCount,
        (
            SELECT CASE
                WHEN COUNT(p) > 0 THEN true
                ELSE false
            END   
            FROM OngParticipant p
            WHERE p.ong.id = :ongId AND p.user.userId = :userId
        ) as isParticipant,
        (
            SELECT CASE    
                WHEN COUNT(f) > 0 THEN true
                ELSE false
            END 
            FROM OngFollower f
            WHERE f.ong.id = :ongId AND f.user.userId = :userId
        ) as isFollowing,
        (
            SELECT CASE    
                WHEN COUNT(i) > 0 THEN true
                ELSE false
            END 
            FROM OngInvite i
            WHERE i.ong.id = :ongId AND i.user.userId = :userId
        ) as isInvited
    FROM Ong o
    WHERE o.id = :ongId 
    """
    )
    OngSummary getOngSummaryById(
            @Param("ongId") UUID ongId,
            @Param("userId") UUID currentUserId
    );

    @Query(value =
            """
            SELECT
                o.id as id,
                o.userOwner.userId as userOwnerId,
                o.name as name,
                o.description as description,
                (
                    SELECT COUNT(p)
                    FROM OngParticipant p
                    WHERE p.ong.id = o.id
                ) as participantsCount,
                (
                    SELECT COUNT(f)  
                    FROM OngFollower f
                    WHERE f.ong.id = o.id
                ) as followersCount,
                (
                    SELECT CASE
                        WHEN COUNT(p) > 0 THEN true
                        ELSE false
                    END   
                    FROM OngParticipant p
                    WHERE p.ong.id = o.id AND p.user.userId = :userId
                ) as isParticipant,
                (
                    SELECT CASE    
                        WHEN COUNT(f) > 0 THEN true
                        ELSE false
                    END 
                    FROM OngFollower f
                    WHERE f.ong.id = o.id AND f.user.userId = :userId
                ) as isFollowing,
                (
                    SELECT CASE    
                        WHEN COUNT(i) > 0 THEN true
                        ELSE false
                    END 
                    FROM OngInvite i
                    WHERE i.ong.id = o.id AND i.user.userId = :userId
                ) as isInvited
            FROM Ong o
            """
    )
    Page<OngSummary> getAllOngSummary(
            @Param("userId") UUID currentUserId,
            Pageable pageable
    );
}
