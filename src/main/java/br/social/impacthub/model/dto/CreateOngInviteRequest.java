package br.social.impacthub.model.dto;

import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record CreateOngInviteRequest(
        @NotNull UUID userID,
        @Pattern(regexp = "adm|mod") String role
) {
}
