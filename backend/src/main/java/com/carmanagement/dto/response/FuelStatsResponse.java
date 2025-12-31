package com.carmanagement.dto.response;

public class FuelStatsResponse {
    
    private Double totalFuel;
    private Double totalCost;
    private Double averageConsumption;
    private Integer entryCount;

    public FuelStatsResponse() {
    }

    public FuelStatsResponse(Double totalFuel, Double totalCost, 
                             Double averageConsumption, Integer entryCount) {
        this.totalFuel = totalFuel;
        this.totalCost = totalCost;
        this.averageConsumption = averageConsumption;
        this.entryCount = entryCount;
    }

    public Double getTotalFuel() {
        return totalFuel;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public Double getAverageConsumption() {
        return averageConsumption;
    }

    public Integer getEntryCount() {
        return entryCount;
    }

    @Override
    public String toString() {
        return "FuelStatsResponse{" +
                "totalFuel=" + totalFuel +
                ", totalCost=" + totalCost +
                ", averageConsumption=" + averageConsumption +
                ", entryCount=" + entryCount +
                '}';
    }
}

