package br.social.impacthub.exception;

public class OngAlreadyExistsException extends RuntimeException {
    public OngAlreadyExistsException(String message) {
        super(message);
    }

    private OngAlreadyExistsException() {}
}
