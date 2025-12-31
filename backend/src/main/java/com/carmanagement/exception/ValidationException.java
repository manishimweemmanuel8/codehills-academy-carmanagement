package com.carmanagement.exception;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class ValidationException extends RuntimeException {
    
    private final Map<String, String> fieldErrors;

    // Constructor with a single field error.
     
    public ValidationException(String field, String message) {
        super("Validation failed: " + message);
        this.fieldErrors = new HashMap<>();
        this.fieldErrors.put(field, message);
    }

    // Constructor with multiple field errors.
     
    public ValidationException(Map<String, String> fieldErrors) {
        super("Validation failed for " + fieldErrors.size() + " field(s)");
        this.fieldErrors = new HashMap<>(fieldErrors);
    }

    // Constructor with message only (no field-level details).
     
    public ValidationException(String message) {
        super(message);
        this.fieldErrors = Collections.emptyMap();
    }

    // Get the field-level validation errors.
     
    public Map<String, String> getFieldErrors() {
        return Collections.unmodifiableMap(fieldErrors);
    }

    // Check if there are field-level errors.
     
    public boolean hasFieldErrors() {
        return !fieldErrors.isEmpty();
    }
}

