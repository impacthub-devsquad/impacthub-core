package br.social.impacthub.events.listeners;

import br.social.impacthub.events.UserProfileDeletedEvent;
import br.social.impacthub.service.security.UserCredentialsService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class UserProfileDeletedEventListener implements ApplicationListener<UserProfileDeletedEvent> {
    private UserCredentialsService userCredentialsService;

    public UserProfileDeletedEventListener(UserCredentialsService userCredentialsService) {
        this.userCredentialsService = userCredentialsService;
    }

    @Override
    public void onApplicationEvent(UserProfileDeletedEvent event) {
        userCredentialsService.deleteById(event.getUserID());
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }
}
