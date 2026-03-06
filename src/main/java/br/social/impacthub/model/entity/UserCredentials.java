package br.social.impacthub.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity @Table(name = "user_credentials")
@Data @AllArgsConstructor @NoArgsConstructor
public class UserCredentials {
    @Column(name = "user_id")
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    private String username;

    @Email
    @NotNull
    private String email;

    @Column(name = "encrypted_password")
    @NotNull
    private String encryptedPassword;
}
