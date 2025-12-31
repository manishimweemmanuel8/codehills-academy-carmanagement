package com.carmanagement.cli.commands;

import com.carmanagement.cli.dto.response.FuelEntryDto;
import com.carmanagement.cli.http.ApiClient;

public class AddFuelCommand implements Command {
    
    @Override
    public String getName() {
        return "add-fuel";
    }

    @Override
    public String getDescription() {
        return "Add a fuel entry for a car";
    }

    @Override
    public String getUsage() {
        return "add-fuel --carId <id> --liters <liters> --price <price> --odometer <km>";
    }

    @Override
    public void execute(ApiClient client, String[] args) throws Exception {
        Long carId = null;
        Double liters = null;
        Double price = null;
        Long odometer = null;
        
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--carId":
                    if (i + 1 < args.length) {
                        try {
                            carId = Long.parseLong(args[++i]);
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("carId must be a number");
                        }
                    }
                    break;
                case "--liters":
                    if (i + 1 < args.length) {
                        try {
                            liters = Double.parseDouble(args[++i]);
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("liters must be a number");
                        }
                    }
                    break;
                case "--price":
                    if (i + 1 < args.length) {
                        try {
                            price = Double.parseDouble(args[++i]);
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("price must be a number");
                        }
                    }
                    break;
                case "--odometer":
                    if (i + 1 < args.length) {
                        try {
                            odometer = Long.parseLong(args[++i]);
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("odometer must be a number");
                        }
                    }
                    break;
            }
        }
        
        if (carId == null) {
            throw new IllegalArgumentException("--carId is required");
        }
        if (liters == null) {
            throw new IllegalArgumentException("--liters is required");
        }
        if (price == null) {
            throw new IllegalArgumentException("--price is required");
        }
        if (odometer == null) {
            throw new IllegalArgumentException("--odometer is required");
        }
        
        FuelEntryDto entry = client.addFuel(carId, liters, price, odometer);
        
        System.out.println();
        System.out.println("Fuel entry added successfully!");
        System.out.println("------------------------------");
        System.out.println("Entry ID:  " + entry.getId());
        System.out.println("Car ID:    " + entry.getCarId());
        System.out.printf("Liters:    %.2f L%n", entry.getLiters());
        System.out.printf("Price:     %.2f%n", entry.getPrice());
        System.out.printf("Odometer:  %d km%n", entry.getOdometer());
    }
}

