package br.social.impacthub.model.dto;

public record EmailValidatorResponse (
        int score,
        String status
) {
}
