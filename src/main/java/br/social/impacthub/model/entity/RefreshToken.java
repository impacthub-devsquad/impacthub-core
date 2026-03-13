package br.social.impacthub.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import java.time.Instant;
import java.util.UUID;

@Data @AllArgsConstructor @NoArgsConstructor
@Entity @Table(name = "refresh_token")
public class RefreshToken {
    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserProfile user;

    @Column(name = "expiration_timestamp")
    private Instant expiration;

    private Boolean revoked;
}
