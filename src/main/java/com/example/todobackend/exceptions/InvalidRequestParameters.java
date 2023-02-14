package com.example.todobackend.exceptions;

public class InvalidRequestParameters extends RuntimeException {
    public InvalidRequestParameters(String message) {
        super(message);
    }
}
