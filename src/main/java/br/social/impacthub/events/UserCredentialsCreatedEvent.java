package br.social.impacthub.events;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class UserCredentialsCreatedEvent extends ApplicationEvent {
    @NotNull private UUID userId;
    @NotBlank private String username;

    public UserCredentialsCreatedEvent(UUID userId, String username, Object source) {
        super(source);
        this.userId = userId;
        this.username = username;
    }
}
