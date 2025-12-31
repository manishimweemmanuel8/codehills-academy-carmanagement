package com.carmanagement.dto.response;

import com.carmanagement.model.FuelEntry;

import java.time.LocalDateTime;

public class FuelEntryResponse {
    
    private Long id;
    private Long carId;
    private Double liters;
    private Double price;
    private Long odometer;
    private LocalDateTime createdAt;

    public FuelEntryResponse() {
    }

    public FuelEntryResponse(Long id, Long carId, Double liters, Double price, 
                             Long odometer, LocalDateTime createdAt) {
        this.id = id;
        this.carId = carId;
        this.liters = liters;
        this.price = price;
        this.odometer = odometer;
        this.createdAt = createdAt;
    }

    public static FuelEntryResponse fromModel(FuelEntry entry) {
        if (entry == null) {
            return null;
        }
        return new FuelEntryResponse(
            entry.getId(),
            entry.getCarId(),
            entry.getLiters(),
            entry.getPrice(),
            entry.getOdometer(),
            entry.getCreatedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getCarId() {
        return carId;
    }

    public Double getLiters() {
        return liters;
    }

    public Double getPrice() {
        return price;
    }

    public Long getOdometer() {
        return odometer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "FuelEntryResponse{" +
                "id=" + id +
                ", carId=" + carId +
                ", liters=" + liters +
                ", price=" + price +
                ", odometer=" + odometer +
                ", createdAt=" + createdAt +
                '}';
    }
}

