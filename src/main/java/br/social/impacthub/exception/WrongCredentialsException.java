package br.social.impacthub.exception;

public class WrongCredentialsException extends RuntimeException {
    public WrongCredentialsException() {
        super("Wrong email or password");
    }

    public WrongCredentialsException(String message) {
        super(message);
    }
}
