package com.carmanagement.cli.commands;

import com.carmanagement.cli.dto.response.FuelStatsDto;
import com.carmanagement.cli.http.ApiClient;

public class FuelStatsCommand implements Command {
    
    @Override
    public String getName() {
        return "fuel-stats";
    }

    @Override
    public String getDescription() {
        return "Get fuel statistics for a car";
    }

    @Override
    public String getUsage() {
        return "fuel-stats --carId <id>";
    }

    @Override
    public void execute(ApiClient client, String[] args) throws Exception {
        Long carId = null;
        
        for (int i = 0; i < args.length; i++) {
            if ("--carId".equals(args[i])) {
                if (i + 1 < args.length) {
                    try {
                        carId = Long.parseLong(args[++i]);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("carId must be a number");
                    }
                }
            }
        }
        
        if (carId == null) {
            throw new IllegalArgumentException("--carId is required");
        }
        
        FuelStatsDto stats = client.getFuelStats(carId);
        
        System.out.printf("Total fuel: %.0f L%n", 
                stats.getTotalFuel() != null ? stats.getTotalFuel() : 0.0);
        System.out.printf("Total cost: %.2f%n", 
                stats.getTotalCost() != null ? stats.getTotalCost() : 0.0);
        System.out.printf("Average consumption: %.1f L/100km%n", 
                stats.getAverageConsumption() != null ? stats.getAverageConsumption() : 0.0);
    }
}
