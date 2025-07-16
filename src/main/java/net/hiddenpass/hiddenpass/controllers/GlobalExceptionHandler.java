package net.hiddenpass.hiddenpass.controllers;

import net.hiddenpass.hiddenpass.responseError.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> generalException(IllegalArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Data error: " + ex.getMessage()

        );
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
