package br.social.impacthub.middleware.handler;

import br.social.impacthub.exception.*;
import br.social.impacthub.model.dto.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardResponse<Void>> handleException(Exception exception){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        StandardResponse.error(exception.getMessage())
                );
    }

    @ExceptionHandler(WrongCredentialsException.class)
    public ResponseEntity<StandardResponse<Void>> handleWrongCredentialsException(WrongCredentialsException exception){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        StandardResponse.error(exception.getMessage())
                );
    }

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<StandardResponse<Void>> handleUserNotAuthenticatedException(UserNotAuthenticatedException exception){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        StandardResponse.error(exception.getMessage())
                );
    }

    @ExceptionHandler(InvalidAccessTokenException.class)
    public ResponseEntity<StandardResponse<Void>> handleInvalidAccessTokenException(InvalidAccessTokenException exception){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        StandardResponse.error(exception.getMessage())
                );
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<StandardResponse<Void>> handleInvalidRefreshTokenException(InvalidRefreshTokenException exception){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        StandardResponse.error(exception.getMessage())
                );
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<StandardResponse<Void>> handleRefreshTokenNotFoundException(RefreshTokenNotFoundException exception){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        StandardResponse.error(exception.getMessage())
                );
    }

    @ExceptionHandler(UsernameOrEmailAlreadyExistsException.class)
    public ResponseEntity<StandardResponse<Void>> handleUsernameOrEmailAlreadyExistsException(UsernameOrEmailAlreadyExistsException exception){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        StandardResponse.error(exception.getMessage())
                );
    }

    @ExceptionHandler(UserNotExistsException.class)
    public ResponseEntity<StandardResponse<Void>> handleUserNotExistsException(UserNotExistsException exception){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        StandardResponse.error(exception.getMessage())
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardResponse<Map<String, String>>> validatorErrorHandler(MethodArgumentNotValidException exception){
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getFieldErrors().forEach((fieldError)->{
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(StandardResponse.fail(errors));
    }
}
