package br.social.impacthub.exception;

public class UserAlreadyInvitedToONG extends RuntimeException {
    public UserAlreadyInvitedToONG(String message){
        super(message);
    }
}
