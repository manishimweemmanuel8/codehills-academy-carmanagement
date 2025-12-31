package com.carmanagement.model;

public class FuelEntry extends BaseModel {
    
    private Long carId;
    private Double liters;
    private Double price;
    private Long odometer;

    public FuelEntry() {
        super();
    }

    public FuelEntry(Long carId, Double liters, Double price, Long odometer) {
        super();
        this.carId = carId;
        this.liters = liters;
        this.price = price;
        this.odometer = odometer;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
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

    @Override
    public String toString() {
        return "FuelEntry{" +
                "id=" + id +
                ", carId=" + carId +
                ", liters=" + liters +
                ", price=" + price +
                ", odometer=" + odometer +
                ", createdAt=" + createdAt +
                '}';
    }
}

