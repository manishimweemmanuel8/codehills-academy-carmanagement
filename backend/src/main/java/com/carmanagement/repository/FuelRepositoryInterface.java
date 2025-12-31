package com.carmanagement.repository;

import com.carmanagement.model.FuelEntry;

import java.util.List;

public interface FuelRepositoryInterface {
    
    FuelEntry save(FuelEntry entry);

    List<FuelEntry> findByCarId(Long carId);

    Long getMaxOdometerByCarId(Long carId);

    long count();

    void clear();
}

