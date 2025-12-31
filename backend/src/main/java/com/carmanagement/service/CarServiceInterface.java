package com.carmanagement.service;

import com.carmanagement.dto.request.CreateCarRequest;
import com.carmanagement.dto.response.CarResponse;

import java.util.List;

public interface CarServiceInterface {
    
    CarResponse createCar(CreateCarRequest request);

    List<CarResponse> getAllCars();

    boolean existsById(Long id);
}

