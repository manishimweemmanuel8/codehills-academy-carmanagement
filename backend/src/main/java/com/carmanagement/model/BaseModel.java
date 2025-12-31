package com.carmanagement.model;

import java.time.LocalDateTime;

public abstract class BaseModel {
    
    protected Long id;
    protected LocalDateTime createdAt;

    protected BaseModel() {
        this.createdAt = LocalDateTime.now();
    }

    protected BaseModel(Long id) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                '}';
    }
}

