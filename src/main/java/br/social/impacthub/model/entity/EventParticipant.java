package br.social.impacthub.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity @Table(name = "event_participant")
@Data @AllArgsConstructor @NoArgsConstructor
@IdClass(EventParticipantId.class)
public class EventParticipant {
    @Id
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserProfile user;
}
