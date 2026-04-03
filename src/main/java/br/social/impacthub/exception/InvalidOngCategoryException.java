package br.social.impacthub.exception;

public class InvalidOngCategoryException extends RuntimeException {
    public InvalidOngCategoryException(String message) {
        super(message);
    }

    public InvalidOngCategoryException(){
        super("Invalid category name");
    }
}
