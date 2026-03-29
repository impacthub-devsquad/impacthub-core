package br.social.impacthub.exception;

public class OngParticipantNotFoundException extends RuntimeException {
    public OngParticipantNotFoundException() {
        super("Ong participant not found");
    }

    public OngParticipantNotFoundException(String message) {
        super(message);
    }
}
