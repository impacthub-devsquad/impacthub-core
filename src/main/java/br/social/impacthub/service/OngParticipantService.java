package br.social.impacthub.service;

import br.social.impacthub.infrastructure.persistence.OngParticipantRepository;
import br.social.impacthub.infrastructure.persistence.OngRepository;
import br.social.impacthub.infrastructure.persistence.UserProfileRepository;
import br.social.impacthub.model.dto.OngParticipantResponse;
import br.social.impacthub.model.dto.PagedResponse;
import br.social.impacthub.model.entity.OngParticipantId;
import br.social.impacthub.service.mapper.OngParticipantMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OngParticipantService {
    private OngParticipantRepository ongParticipantRepository;
    private OngParticipantMapper ongParticipantMapper;
    private OngRepository ongRepository;
    private UserProfileRepository userProfileRepository;

    public OngParticipantService(OngParticipantRepository ongParticipantRepository, OngParticipantMapper ongParticipantMapper) {
        this.ongParticipantRepository = ongParticipantRepository;
        this.ongParticipantMapper = ongParticipantMapper;
    }

    public PagedResponse<OngParticipantResponse> getAll(UUID ongId, Pageable pageable){
        return null;
    }

    public OngParticipantResponse getByUserId(UUID ongId, UUID userId){
        return null;
    }

    public void delete(UUID ongId, UUID userId, UUID authenticatedUserId) {
    }

    public void inviteParticipant(UUID ongId, UUID userId, Integer roleId, UUID authenticatedUserId) {
    }


    public void deleteInvite(UUID ongId, UUID userId, UUID authenticatedUserId) {
        ongParticipantRepository.deleteById(new OngParticipantId(ongRepository.getReferenceById(ongId), userProfileRepository.getReferenceById(userId)));
    }
}
