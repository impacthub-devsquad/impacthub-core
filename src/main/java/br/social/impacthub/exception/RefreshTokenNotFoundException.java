package br.social.impacthub.exception;

public class RefreshTokenNotFoundException extends RuntimeException {
    public RefreshTokenNotFoundException() {
        super("Refresh token not found");
    }

    public RefreshTokenNotFoundException(String message) {
        super(message);
    }
}
