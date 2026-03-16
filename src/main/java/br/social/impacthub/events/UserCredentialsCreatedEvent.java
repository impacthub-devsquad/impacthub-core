package br.social.impacthub.events;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class UserCredentialsCreatedEvent extends ApplicationEvent {
    @NotNull private UUID userId;
    @NotBlank private String username;
    @Email private String email;

    public UserCredentialsCreatedEvent(UUID userId, String username, String email, Object source) {
        super(source);
        this.userId = userId;
        this.username = username;
        this.email = email;
    }
}
