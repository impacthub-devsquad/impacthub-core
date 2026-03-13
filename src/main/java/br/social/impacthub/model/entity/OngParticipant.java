package br.social.impacthub.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;


@Entity
@Table(name = "ong_participant")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class OngParticipant {

    @ManyToOne
    @MapsId("ongId")
    @JoinColumn(name = "ong_id")
    private Ong ong;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserProfile user;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}

