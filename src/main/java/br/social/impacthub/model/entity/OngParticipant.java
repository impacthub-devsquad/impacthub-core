package br.social.impacthub.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "ong_participant")
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(OngParticipantId.class)

public class OngParticipant {
    @Id
    @ManyToOne
    @JoinColumn(name = "ong_id")
    private Ong ong;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserProfile user;

    @ManyToOne
    @JoinColumn(name = "role")
    private OngParticipantCategory OngParticipant;

}

