package com.carmanagement.exception;


public class NotFoundException extends RuntimeException {
    
    private final String resourceName;
    private final Object resourceId;

    // Constructor with message only.
     
    public NotFoundException(String message) {
        super(message);
        this.resourceName = null;
        this.resourceId = null;
    }

    // Constructor with resource details.
     
    public NotFoundException(String resourceName, Object resourceId) {
        super(String.format("%s with ID '%s' not found", resourceName, resourceId));
        this.resourceName = resourceName;
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public Object getResourceId() {
        return resourceId;
    }
}

