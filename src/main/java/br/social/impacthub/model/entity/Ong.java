package br.social.impacthub.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "ong")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ong {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    @Column(name = "ong_id")
    private UUID ongId;

    @NotNull
    private String name;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @OneToMany(mappedBy = "ong")
    private Set<Event> events;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private OngCategory category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserProfile userProfile;
}