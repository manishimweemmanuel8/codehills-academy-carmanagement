package com.carmanagement.servlet;

import com.carmanagement.dto.response.FuelStatsResponse;
import com.carmanagement.exception.BadRequestException;
import com.carmanagement.service.FuelService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class FuelStatsServlet extends BaseServlet {
    
    private final FuelService fuelService;

    public FuelStatsServlet(FuelService fuelService) {
        this.fuelService = fuelService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String carIdParam = req.getParameter("carId");
            
            if (carIdParam == null || carIdParam.trim().isEmpty()) {
                throw new BadRequestException("Query parameter 'carId' is required");
            }
            
            Long carId;
            try {
                carId = Long.parseLong(carIdParam.trim());
            } catch (NumberFormatException e) {
                throw new BadRequestException("Invalid carId format: must be a number");
            }
            
            if (carId <= 0) {
                throw new BadRequestException("carId must be a positive number");
            }
            
            FuelStatsResponse stats = fuelService.getFuelStats(carId);
            sendJson(resp, HttpServletResponse.SC_OK, stats);
            
        } catch (Exception e) {
            sendError(resp, e);
        }
    }
}

