package com.carmanagement.exception;


public class BadRequestException extends RuntimeException {

    // Constructor with message.
     
    public BadRequestException(String message) {
        super(message);
    }

    // Constructor with message and cause.
     
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}

