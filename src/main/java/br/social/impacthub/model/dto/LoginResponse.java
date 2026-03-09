package br.social.impacthub.model.dto;

public record LoginResponse (
        String accessToken,
        String refreshToken
) {

}
