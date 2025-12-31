package com.carmanagement.service;

import com.carmanagement.dto.request.CreateCarRequest;
import com.carmanagement.dto.response.CarResponse;
import com.carmanagement.exception.ValidationException;
import com.carmanagement.model.Car;
import com.carmanagement.repository.CarRepository;

import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class CarService implements CarServiceInterface {
    
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public CarResponse createCar(CreateCarRequest request) {
        validateCreateCarRequest(request);
        
        Car car = new Car(
            request.getBrand().trim(),
            request.getModel().trim(),
            request.getYear()
        );
        
        Car savedCar = carRepository.save(car);
        return CarResponse.fromModel(savedCar);
    }

    @Override
    public List<CarResponse> getAllCars() {
        return carRepository.findAll().stream()
                .map(CarResponse::fromModel)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return carRepository.existsById(id);
    }

    private void validateCreateCarRequest(CreateCarRequest request) {
        Map<String, String> errors = new HashMap<>();
        
        if (request.getBrand() == null || request.getBrand().trim().isEmpty()) {
            errors.put("brand", "Brand is required");
        }
        
        if (request.getModel() == null || request.getModel().trim().isEmpty()) {
            errors.put("model", "Model is required");
        }
        
        if (request.getYear() == null) {
            errors.put("year", "Year is required");
        } else {
            int currentYear = Year.now().getValue();
            if (request.getYear() < 1900) {
                errors.put("year", "Year must be 1900 or later");
            } else if (request.getYear() > currentYear + 1) {
                errors.put("year", "Year cannot be more than " + (currentYear + 1));
            }
        }
        
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}

