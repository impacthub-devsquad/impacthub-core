package br.social.impacthub.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor

public class EventParticipant {
//    @Id
//    @ManyToOne
//    @JoinColumn(name = "event_id", referencedColumnName = "id")
//    private Event event;
//
//    @Id
//    @ManyToOne
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    private UserProfile user;
}
