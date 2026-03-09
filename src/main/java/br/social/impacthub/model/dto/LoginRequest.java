package br.social.impacthub.model.dto;

public record LoginRequest (
        String email,
        String password
) {
}
