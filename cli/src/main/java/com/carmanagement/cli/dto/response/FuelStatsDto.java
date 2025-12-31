package com.carmanagement.cli.dto.response;

public class FuelStatsDto {
    
    private Double totalFuel;
    private Double totalCost;
    private Double averageConsumption;
    private Integer entryCount;

    public FuelStatsDto() {
    }

    public FuelStatsDto(Double totalFuel, Double totalCost, 
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
        return String.format(
            "Total fuel: %.2f L%n" +
            "Total cost: $%.2f%n" +
            "Average consumption: %.2f L/100km%n" +
            "Entries recorded: %d",
            totalFuel != null ? totalFuel : 0.0,
            totalCost != null ? totalCost : 0.0,
            averageConsumption != null ? averageConsumption : 0.0,
            entryCount != null ? entryCount : 0
        );
    }
}

