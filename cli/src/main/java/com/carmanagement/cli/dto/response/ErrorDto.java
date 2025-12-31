package com.carmanagement.cli.dto.response;

import java.util.Map;

public class ErrorDto {
    
    private int status;
    private String error;
    private String message;
    private String timestamp;
    private Map<String, String> fieldErrors;

    public ErrorDto() {
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Error ").append(status).append(": ").append(message);
        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            sb.append("\nField errors:");
            fieldErrors.forEach((field, err) -> 
                sb.append("\n  - ").append(field).append(": ").append(err));
        }
        return sb.toString();
    }
}

