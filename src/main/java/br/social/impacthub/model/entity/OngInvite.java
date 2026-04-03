package br.social.impacthub.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity @Table(name = "ong_invite")
@IdClass(OngInviteId.class)
@Data @AllArgsConstructor @NoArgsConstructor
public class OngInvite {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserProfile user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ong_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Ong ong;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private OngParticipantRole role;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserProfile createdBy;
}
