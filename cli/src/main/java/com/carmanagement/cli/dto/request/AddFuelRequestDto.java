package com.carmanagement.cli.dto.request;

// Request DTO for adding fuel entry via CLI.
public class AddFuelRequestDto {
    
    private Double liters;
    private Double price;
    private Long odometer;

    public AddFuelRequestDto() {
    }

    public AddFuelRequestDto(Double liters, Double price, Long odometer) {
        this.liters = liters;
        this.price = price;
        this.odometer = odometer;
    }

    public Double getLiters() {
        return liters;
    }

    public void setLiters(Double liters) {
        this.liters = liters;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getOdometer() {
        return odometer;
    }

    public void setOdometer(Long odometer) {
        this.odometer = odometer;
    }
}

