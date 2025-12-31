package com.carmanagement.dto.response;

import com.carmanagement.model.Car;

import java.time.LocalDateTime;

public class CarResponse {
    
    private Long id;
    private String brand;
    private String model;
    private Integer year;
    private LocalDateTime createdAt;

    public CarResponse() {
    }

    public CarResponse(Long id, String brand, String model, Integer year, LocalDateTime createdAt) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.createdAt = createdAt;
    }

    public static CarResponse fromModel(Car car) {
        if (car == null) {
            return null;
        }
        return new CarResponse(
            car.getId(),
            car.getBrand(),
            car.getModel(),
            car.getYear(),
            car.getCreatedAt()
        );
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "CarResponse{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", createdAt=" + createdAt +
                '}';
    }
}

