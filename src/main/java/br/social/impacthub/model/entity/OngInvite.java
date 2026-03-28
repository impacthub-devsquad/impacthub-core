package br.social.impacthub.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity @Table(name = "ong_invite")
@IdClass(OngInviteId.class)
@Data @AllArgsConstructor @NoArgsConstructor
public class OngInvite {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserProfile user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ong_id")
    private Ong ong;

    @ManyToOne
    @JoinColumn(name = "ong_participant_role")
    private OngParticipantRole role;

    @Column(name = "created_at")
    private Instant createdAt;
}
