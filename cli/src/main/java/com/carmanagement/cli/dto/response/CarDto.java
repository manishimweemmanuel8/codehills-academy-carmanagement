package com.carmanagement.cli.dto.response;

public class CarDto {
    
    private Long id;
    private String brand;
    private String model;
    private Integer year;
    private String createdAt;

    public CarDto() {
    }

    public CarDto(Long id, String brand, String model, Integer year, String createdAt) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public Integer getYear() {
        return year;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return String.format("Car #%d: %s %s (%d)", id, brand, model, year);
    }
}

