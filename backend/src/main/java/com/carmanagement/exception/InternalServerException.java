package com.carmanagement.exception;

public class InternalServerException extends RuntimeException {

    // Constructor with message.
     
    public InternalServerException(String message) {
        super(message);
    }

    // Constructor with message and cause.
     
    public InternalServerException(String message, Throwable cause) {
        super(message, cause);
    }
}

