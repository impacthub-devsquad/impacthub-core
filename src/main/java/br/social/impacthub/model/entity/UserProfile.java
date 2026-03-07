package br.social.impacthub.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity @Table(name = "user_profile")
@Data @AllArgsConstructor @NoArgsConstructor
public class UserProfile {
    @Id
    @Column(name = "user_id")
    @NotNull
    private UUID userId;

    @NotNull
    private String username;
}
