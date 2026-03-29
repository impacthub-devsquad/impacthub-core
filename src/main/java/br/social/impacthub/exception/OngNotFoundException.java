package br.social.impacthub.exception;

public class OngNotFoundException extends RuntimeException {
    public OngNotFoundException() {
        super("Ong not found");
    }

    public OngNotFoundException(String message) {
        super(message);
    }
}
