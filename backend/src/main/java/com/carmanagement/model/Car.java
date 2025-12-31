package com.carmanagement.model;

public class Car extends BaseModel {
    
    private String brand;
    private String model;
    private Integer year;

    public Car() {
        super();
    }

    public Car(String brand, String model, Integer year) {
        super();
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

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", createdAt=" + createdAt +
                '}';
    }
}

