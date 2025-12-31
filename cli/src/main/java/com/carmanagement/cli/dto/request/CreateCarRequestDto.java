package com.carmanagement.cli.dto.request;

// Request DTO for creating a car via CLI.
public class CreateCarRequestDto {
    
    private String brand;
    private String model;
    private Integer year;

    public CreateCarRequestDto() {
    }

    public CreateCarRequestDto(String brand, String model, Integer year) {
        this.brand = brand;
        this.model = model;
        this.year = year;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}

