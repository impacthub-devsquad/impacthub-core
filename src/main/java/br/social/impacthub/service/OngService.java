package br.social.impacthub.service;

import br.social.impacthub.model.dto.CreateOngRequest;
import br.social.impacthub.model.dto.OngResponse;
import br.social.impacthub.model.dto.PagedResponse;
import br.social.impacthub.model.dto.UpdateOngRequest;
import br.social.impacthub.service.mapper.OngMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OngService {
    private OngMapper ongMapper;

    public OngService(OngMapper ongMapper) {
        this.ongMapper = ongMapper;
    }

    public PagedResponse<OngResponse> getAll(Pageable pageable){
        return null;
    }

    public OngResponse getById(UUID ongId){
        return null;
    }

    public OngResponse create(@Valid CreateOngRequest request, UUID authenticatedUserID) {
        return null;
    }

    public void delete(UUID ongId, UUID authenticatedUserID) {
        return;
    }

    public void followOng(UUID authenticatedUserId, UUID ongId) {
    }

    public void unfollowOng(UUID authenticatedUserId, UUID ongId) {
    }

    public OngResponse update(@Valid UpdateOngRequest request, UUID userId) {
        return null;
    }
}
