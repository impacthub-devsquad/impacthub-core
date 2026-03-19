package br.social.impacthub.events.listeners;

import br.social.impacthub.events.UserProfileUpdatedEvent;
import br.social.impacthub.service.security.UserCredentialsService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class UserProfileUpdatedEventListener implements ApplicationListener<UserProfileUpdatedEvent> {
    private UserCredentialsService userCredentialsService;

    public UserProfileUpdatedEventListener(UserCredentialsService userCredentialsService) {
        this.userCredentialsService = userCredentialsService;
    }

    @Override
    public void onApplicationEvent(UserProfileUpdatedEvent event) {
        userCredentialsService.updateProfile(event.getUserProfile());
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }
}
