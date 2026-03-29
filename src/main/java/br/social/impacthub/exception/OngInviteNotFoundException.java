package br.social.impacthub.exception;

public class OngInviteNotFoundException extends RuntimeException {
    public OngInviteNotFoundException(String message) {
        super(message);
    }

    public OngInviteNotFoundException() {
        super("OngInvite not found");
    }
}
