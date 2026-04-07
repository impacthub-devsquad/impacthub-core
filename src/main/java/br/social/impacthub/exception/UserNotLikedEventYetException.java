package br.social.impacthub.exception;

public class UserNotLikedEventYetException extends RuntimeException {
    public UserNotLikedEventYetException() {
        super("User not liked this event yet");
    }
}
