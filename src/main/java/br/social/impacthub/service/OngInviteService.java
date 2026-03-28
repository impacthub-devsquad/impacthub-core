package br.social.impacthub.service;

import br.social.impacthub.model.dto.CreateOngInviteRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OngInviteService {
    public void create(@Valid CreateOngInviteRequest request, UUID ongId, UUID authenticatedUserId) {
        return;
    }

    public void delete(UUID inviteId, UUID authenticatedUserId) {
        return;
    }

    public void accept(UUID inviteId, UUID authenticatedUserId) {

    }

    public void recuse(UUID inviteId, UUID authenticatedUserId) {

    }
}
