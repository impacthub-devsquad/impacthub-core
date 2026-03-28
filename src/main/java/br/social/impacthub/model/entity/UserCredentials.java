package br.social.impacthub.model.entity;


import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity @Table(name = "user_credentials")
@Data @AllArgsConstructor @NoArgsConstructor
public class UserCredentials implements UserDetails {
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

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "created_at")
    private Instant createdAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN){
            return List.of(
                    new SimpleGrantedAuthority("ADMIN"),
                    new SimpleGrantedAuthority("USER")
            );
        }
        else {
            return List.of(
                    new SimpleGrantedAuthority("USER")
            );
        }
    }

    @Override
    public @Nullable String getPassword() {
        return this.encryptedPassword;
    }
}
