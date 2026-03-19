package br.social.impacthub.events;

import br.social.impacthub.model.entity.UserProfile;
import br.social.impacthub.service.UserProfileService;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;
import java.util.UUID;

@Getter
public class UserProfileUpdatedEvent extends ApplicationEvent {
    private UserProfile userProfile;

    public UserProfileUpdatedEvent(UserProfile userProfile, Object source) {
        super(source);
        this.userProfile = userProfile;
    }
}
