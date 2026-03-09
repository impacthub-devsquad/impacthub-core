package br.social.impacthub.exception;

public class InvalidAccessTokenException extends RuntimeException {
    public InvalidAccessTokenException() {
        super("Invalid access token");
    }

    public InvalidAccessTokenException(String message) {
        super(message);
    }
}
