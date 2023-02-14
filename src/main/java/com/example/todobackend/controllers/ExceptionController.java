package com.example.todobackend.controllers;

import com.example.todobackend.exceptions.ErrorResponse;
import com.example.todobackend.exceptions.InvalidRequestParameters;
import com.example.todobackend.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        exception.getMessage(),
                        request.getDescription(false)));
    }

    @ExceptionHandler({InvalidRequestParameters.class})
    public ResponseEntity<ErrorResponse> handleInvalidRequestParameters(InvalidRequestParameters exception, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        exception.getMessage(),
                        request.getDescription(false)));
    }

}
