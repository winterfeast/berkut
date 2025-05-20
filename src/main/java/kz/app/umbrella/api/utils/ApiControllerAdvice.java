package kz.app.umbrella.api.utils;

import kz.app.umbrella.api.exception.UserAlreadyExistsException;
import kz.app.umbrella.api.exception.UserNotFoundException;
import kz.app.umbrella.api.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> alreadyExistException(UserAlreadyExistsException ex) {
        return new ResponseEntity<>(
                new ErrorResponse("user-already-exists", ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(
                new ErrorResponse("user-not-found", ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

}
