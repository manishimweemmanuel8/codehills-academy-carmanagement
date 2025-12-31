package com.carmanagement.cli.dto.response;

public class FuelEntryDto {
    
    private Long id;
    private Long carId;
    private Double liters;
    private Double price;
    private Long odometer;
    private String createdAt;

    public FuelEntryDto() {
    }

    public FuelEntryDto(Long id, Long carId, Double liters, Double price, 
                        Long odometer, String createdAt) {
        this.id = id;
        this.carId = carId;
        this.liters = liters;
        this.price = price;
        this.odometer = odometer;
        this.createdAt = createdAt;
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

    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return String.format("Fuel Entry #%d: %.2f L @ $%.2f (odometer: %d km)", 
                id, liters, price, odometer);
    }
}

