package br.social.impacthub.events.listeners;

import br.social.impacthub.events.UserCredentialsCreatedEvent;
import br.social.impacthub.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class UserCredentialsCreatedEventListener implements ApplicationListener<UserCredentialsCreatedEvent> {
    private UserProfileService userProfileService;

    public UserCredentialsCreatedEventListener(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @Override
    public void onApplicationEvent(@Valid UserCredentialsCreatedEvent event) {
        userProfileService.create(event.getUserId(), event.getUsername(), event.getEmail());
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }
}
