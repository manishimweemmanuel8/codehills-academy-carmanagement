package com.carmanagement.service;

import com.carmanagement.dto.request.AddFuelRequest;
import com.carmanagement.dto.response.FuelEntryResponse;
import com.carmanagement.dto.response.FuelStatsResponse;
import com.carmanagement.exception.NotFoundException;
import com.carmanagement.exception.ValidationException;
import com.carmanagement.model.FuelEntry;
import com.carmanagement.repository.CarRepository;
import com.carmanagement.repository.FuelRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FuelService implements FuelServiceInterface {
    
    private static final double CONSUMPTION_MULTIPLIER = 100.0;
    private static final double ROUNDING_PRECISION = 100.0;
    
    private final FuelRepository fuelRepository;
    private final CarRepository carRepository;

    public FuelService(FuelRepository fuelRepository, CarRepository carRepository) {
        this.fuelRepository = fuelRepository;
        this.carRepository = carRepository;
    }

    @Override
    public FuelEntryResponse addFuelEntry(Long carId, AddFuelRequest request) {
        if (!carRepository.existsById(carId)) {
            throw new NotFoundException("Car", carId);
        }
        
        validateAddFuelRequest(request);
        
        Long lastOdometer = fuelRepository.getMaxOdometerByCarId(carId);
        if (lastOdometer != null && request.getOdometer() < lastOdometer) {
            Map<String, String> errors = new HashMap<>();
            errors.put("odometer", "Odometer reading (" + request.getOdometer() + 
                    ") must be greater than or equal to the last reading (" + lastOdometer + ")");
            throw new ValidationException(errors);
        }
        
        FuelEntry entry = new FuelEntry(
            carId,
            request.getLiters(),
            request.getPrice(),
            request.getOdometer()
        );
        
        FuelEntry savedEntry = fuelRepository.save(entry);
        return FuelEntryResponse.fromModel(savedEntry);
    }

    @Override
    public FuelStatsResponse getFuelStats(Long carId) {
        if (!carRepository.existsById(carId)) {
            throw new NotFoundException("Car", carId);
        }
        
        List<FuelEntry> entries = fuelRepository.findByCarId(carId);
        
        if (entries.isEmpty()) {
            return new FuelStatsResponse(0.0, 0.0, 0.0, 0);
        }
        
        double totalFuel = entries.stream()
                .mapToDouble(FuelEntry::getLiters)
                .sum();
        
        double totalCost = entries.stream()
                .mapToDouble(FuelEntry::getPrice)
                .sum();
        
        double averageConsumption = 0.0;
        
        if (entries.size() >= 2) {
            long minOdometer = entries.get(0).getOdometer();
            long maxOdometer = entries.get(entries.size() - 1).getOdometer();
            long distance = maxOdometer - minOdometer;
            
            if (distance > 0) {
                double fuelUsed = entries.stream()
                        .skip(1)
                        .mapToDouble(FuelEntry::getLiters)
                        .sum();
                
                averageConsumption = (fuelUsed / distance) * CONSUMPTION_MULTIPLIER;
            }
        }
        
        return new FuelStatsResponse(
            Math.round(totalFuel * ROUNDING_PRECISION) / ROUNDING_PRECISION,
            Math.round(totalCost * ROUNDING_PRECISION) / ROUNDING_PRECISION,
            Math.round(averageConsumption * ROUNDING_PRECISION) / ROUNDING_PRECISION,
            entries.size()
        );
    }

    private void validateAddFuelRequest(AddFuelRequest request) {
        Map<String, String> errors = new HashMap<>();
        
        if (request.getLiters() == null) {
            errors.put("liters", "Liters is required");
        } else if (request.getLiters() <= 0) {
            errors.put("liters", "Liters must be greater than 0");
        }
        
        if (request.getPrice() == null) {
            errors.put("price", "Price is required");
        } else if (request.getPrice() < 0) {
            errors.put("price", "Price cannot be negative");
        }
        
        if (request.getOdometer() == null) {
            errors.put("odometer", "Odometer is required");
        } else if (request.getOdometer() <= 0) {
            errors.put("odometer", "Odometer must be greater than 0");
        }
        
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}

