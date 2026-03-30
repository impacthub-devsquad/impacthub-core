package br.social.impacthub.exception;

import jakarta.validation.constraints.NotBlank;

public class InvalidOngParticipantRoleException extends RuntimeException {
    public InvalidOngParticipantRoleException(String message) {
        super(message);
    }
}
