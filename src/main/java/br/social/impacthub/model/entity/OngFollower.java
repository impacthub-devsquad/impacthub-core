package br.social.impacthub.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Entity @Table(name = "ong_follower")
@IdClass(OngFollowerId.class)
@Data
public class OngFollower {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ong_id")
    private Ong ong;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserProfile user;

    private Instant createdAt;
}
