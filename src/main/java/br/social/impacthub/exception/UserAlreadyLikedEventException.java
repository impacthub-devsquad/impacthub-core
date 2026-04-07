package br.social.impacthub.exception;

public class UserAlreadyLikedEventException extends RuntimeException {
    public UserAlreadyLikedEventException() {
        super("User is already liked this event");
    }
}
