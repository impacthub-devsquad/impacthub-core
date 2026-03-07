package br.social.impacthub.exception;

public class InvalidAccessToken extends RuntimeException {
    public InvalidAccessToken(String message) {
        super(message);
    }
}
