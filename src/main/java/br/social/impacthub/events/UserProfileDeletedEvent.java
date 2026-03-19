package br.social.impacthub.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class UserProfileDeletedEvent extends ApplicationEvent {
    private UUID userID;

    public UserProfileDeletedEvent(UUID userId, Object source) {
        super(source);
        this.userID = userId;
    }
}
