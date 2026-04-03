package br.social.impacthub.middleware.handler;

import br.social.impacthub.exception.*;
import br.social.impacthub.model.dto.StandardResponse;
import br.social.impacthub.model.entity.OngCategory;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
                        StandardResponse.fail(exception.getMessage())
                );
    }

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<StandardResponse<Void>> handleUserNotAuthenticatedException(UserNotAuthenticatedException exception){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        StandardResponse.fail(exception.getMessage())
                );
    }

    @ExceptionHandler(InvalidAccessTokenException.class)
    public ResponseEntity<StandardResponse<Void>> handleInvalidAccessTokenException(InvalidAccessTokenException exception){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        StandardResponse.fail(exception.getMessage())
                );
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<StandardResponse<Void>> handleInvalidRefreshTokenException(InvalidRefreshTokenException exception){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        StandardResponse.fail(exception.getMessage())
                );
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<StandardResponse<Void>> handleRefreshTokenNotFoundException(RefreshTokenNotFoundException exception){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        StandardResponse.fail(exception.getMessage())
                );
    }

    @ExceptionHandler(UsernameOrEmailAlreadyExistsException.class)
    public ResponseEntity<StandardResponse<Void>> handleUsernameOrEmailAlreadyExistsException(UsernameOrEmailAlreadyExistsException exception){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        StandardResponse.fail(exception.getMessage())
                );
    }

    @ExceptionHandler(UserNotExistsException.class)
    public ResponseEntity<StandardResponse<Void>> handleUserNotExistsException(UserNotExistsException exception){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        StandardResponse.fail(exception.getMessage())
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
                .body(StandardResponse.fail("Malformed data", errors));
    }

    @ExceptionHandler(InvalidEmailAddressException.class)
    public ResponseEntity<StandardResponse<Void>> handleInvalidEmailAddressException(InvalidEmailAddressException exception){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        StandardResponse.fail(exception.getMessage())
                );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardResponse<Void>> handleEntityNotFound(EntityNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        StandardResponse.fail(exception.getMessage())
                );
    }

    @ExceptionHandler(OngAlreadyExistsException.class)
    public ResponseEntity<StandardResponse<Void>> handleOngAlreadyExists(OngAlreadyExistsException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        StandardResponse.fail(exception.getMessage())
                );
    }

    @ExceptionHandler(UserAlreadyFollowingONG.class)
    public ResponseEntity<StandardResponse<Void>> handleUserAlreadyFollowingONG(UserAlreadyFollowingONG exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        StandardResponse.fail(exception.getMessage())
                );
    }

    @ExceptionHandler(UserAlreadyInvitedToONG.class)
    public ResponseEntity<StandardResponse<Void>> handleUserAlreadyInvitedToONG(UserAlreadyInvitedToONG exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        StandardResponse.fail(exception.getMessage())
                );
    }

    @ExceptionHandler(UserNotFollowingONG.class)
    public ResponseEntity<StandardResponse<Void>> handleUserNotFollowingONG(UserNotFollowingONG exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        StandardResponse.fail(exception.getMessage())
                );
    }

    @ExceptionHandler(OngNotFoundException.class)
    public ResponseEntity<StandardResponse<Void>> handleOngNotFound(OngNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        StandardResponse.fail(exception.getMessage())
                );
    }

    @ExceptionHandler(OngInviteNotFoundException.class)
    public ResponseEntity<StandardResponse<Void>> handleOngInviteNotFound(OngInviteNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        StandardResponse.fail(exception.getMessage())
                );
    }

    @ExceptionHandler(InvalidOngCategoryException.class)
    public ResponseEntity<StandardResponse<Map<String, List<String>>>> handleInvalidOngCategory(InvalidOngCategoryException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        StandardResponse.fail(
                                exception.getMessage(),
                                Map.of(
                                        "valid categories",
                                        Arrays.stream(OngCategory.Values.values())
                                                .map(category -> category.name)
                                                .toList()
                                )
                        )
                );
    }
}
