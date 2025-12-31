package com.carmanagement.service;

import com.carmanagement.dto.request.AddFuelRequest;
import com.carmanagement.dto.response.FuelEntryResponse;
import com.carmanagement.dto.response.FuelStatsResponse;

public interface FuelServiceInterface {
    
    FuelEntryResponse addFuelEntry(Long carId, AddFuelRequest request);

    FuelStatsResponse getFuelStats(Long carId);
}

