package com.carmanagement.servlet;

import com.carmanagement.dto.request.AddFuelRequest;
import com.carmanagement.dto.request.CreateCarRequest;
import com.carmanagement.dto.response.CarResponse;
import com.carmanagement.dto.response.FuelEntryResponse;
import com.carmanagement.dto.response.FuelStatsResponse;
import com.carmanagement.exception.BadRequestException;
import com.carmanagement.service.CarService;
import com.carmanagement.service.FuelService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class CarsApiServlet extends BaseServlet {
    
    private final CarService carService;
    private final FuelService fuelService;

    public CarsApiServlet(CarService carService, FuelService fuelService) {
        this.carService = carService;
        this.fuelService = fuelService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String pathInfo = req.getPathInfo();
            
            if (pathInfo == null || pathInfo.equals("/")) {
                List<CarResponse> cars = carService.getAllCars();
                sendJson(resp, HttpServletResponse.SC_OK, cars);
            } else if (pathInfo.matches("/\\d+/fuel/stats")) {
                Long carId = extractCarIdFromPath(pathInfo);
                FuelStatsResponse stats = fuelService.getFuelStats(carId);
                sendJson(resp, HttpServletResponse.SC_OK, stats);
            } else {
                throw new BadRequestException("Unknown endpoint: GET /api/cars" + pathInfo);
            }
        } catch (Exception e) {
            sendError(resp, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String pathInfo = req.getPathInfo();
            
            if (pathInfo == null || pathInfo.equals("/")) {
                CreateCarRequest request = parseRequestBody(req, CreateCarRequest.class);
                CarResponse car = carService.createCar(request);
                sendJson(resp, HttpServletResponse.SC_CREATED, car);
            } else if (pathInfo.matches("/\\d+/fuel")) {
                Long carId = extractCarIdFromPath(pathInfo);
                AddFuelRequest request = parseRequestBody(req, AddFuelRequest.class);
                FuelEntryResponse entry = fuelService.addFuelEntry(carId, request);
                sendJson(resp, HttpServletResponse.SC_CREATED, entry);
            } else {
                throw new BadRequestException("Unknown endpoint: POST /api/cars" + pathInfo);
            }
        } catch (Exception e) {
            sendError(resp, e);
        }
    }

    private Long extractCarIdFromPath(String pathInfo) {
        if (pathInfo == null || pathInfo.length() < 2) {
            throw new BadRequestException("Car ID is required in the path");
        }
        
        String[] parts = pathInfo.substring(1).split("/");
        if (parts.length < 1) {
            throw new BadRequestException("Car ID is required in the path");
        }
        
        try {
            Long carId = Long.parseLong(parts[0]);
            if (carId <= 0) {
                throw new BadRequestException("Car ID must be a positive number, got: " + carId);
            }
            return carId;
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid car ID format: " + parts[0]);
        }
    }
}

