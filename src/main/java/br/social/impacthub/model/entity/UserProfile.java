package br.social.impacthub.model.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity @Table(name = "user_profile")
@Data @AllArgsConstructor @NoArgsConstructor
public class UserProfile {
    @Id
    private UUID userId;

    @NotBlank
    @Column(name = "username", unique = true)
    private String username;

    @NotNull
    private String name;

    @Email
    private String email;

    @NotNull
    private String description;

    @Column(name = "created_at")
    private Instant createdAt;
}
