package com.carmanagement.cli.commands;

import com.carmanagement.cli.dto.response.CarDto;
import com.carmanagement.cli.http.ApiClient;

public class CreateCarCommand implements Command {
    
    @Override
    public String getName() {
        return "create-car";
    }

    @Override
    public String getDescription() {
        return "Create a new car";
    }

    @Override
    public String getUsage() {
        return "create-car --brand <brand> --model <model> --year <year>";
    }

    @Override
    public void execute(ApiClient client, String[] args) throws Exception {
        String brand = null;
        String model = null;
        Integer year = null;
        
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--brand":
                    if (i + 1 < args.length) {
                        brand = args[++i];
                    }
                    break;
                case "--model":
                    if (i + 1 < args.length) {
                        model = args[++i];
                    }
                    break;
                case "--year":
                    if (i + 1 < args.length) {
                        try {
                            year = Integer.parseInt(args[++i]);
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("Year must be a number");
                        }
                    }
                    break;
            }
        }
        
        if (brand == null || brand.isEmpty()) {
            throw new IllegalArgumentException("--brand is required");
        }
        if (model == null || model.isEmpty()) {
            throw new IllegalArgumentException("--model is required");
        }
        if (year == null) {
            throw new IllegalArgumentException("--year is required");
        }
        
        CarDto car = client.createCar(brand, model, year);
        
        System.out.println();
        System.out.println("Car created successfully!");
        System.out.println("-------------------------");
        System.out.println("ID:    " + car.getId());
        System.out.println("Brand: " + car.getBrand());
        System.out.println("Model: " + car.getModel());
        System.out.println("Year:  " + car.getYear());
    }
}

