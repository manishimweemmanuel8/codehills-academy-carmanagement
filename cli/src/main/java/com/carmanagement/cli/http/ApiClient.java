package com.carmanagement.cli.http;

import com.carmanagement.cli.dto.request.AddFuelRequestDto;
import com.carmanagement.cli.dto.request.CreateCarRequestDto;
import com.carmanagement.cli.dto.response.CarDto;
import com.carmanagement.cli.dto.response.ErrorDto;
import com.carmanagement.cli.dto.response.FuelEntryDto;
import com.carmanagement.cli.dto.response.FuelStatsDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ApiClient {
    
    private static final String DEFAULT_BASE_URL = "http://localhost:8080";
    private static final int TIMEOUT_SECONDS = 30;
    private static final Duration TIMEOUT = Duration.ofSeconds(TIMEOUT_SECONDS);
    
    private final HttpClient client;
    private final String baseUrl;
    private final Gson gson;

    public ApiClient() {
        this(DEFAULT_BASE_URL);
    }
    
    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.client = HttpClient.newBuilder()
                .connectTimeout(TIMEOUT)
                .build();
        this.gson = new GsonBuilder().create();
    }

    public CarDto createCar(String brand, String model, int year) throws ApiException {
        CreateCarRequestDto requestDto = new CreateCarRequestDto(brand, model, year);
        String json = gson.toJson(requestDto);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/cars"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .timeout(TIMEOUT)
                .build();
        
        return executeRequest(request, CarDto.class);
    }

    public FuelEntryDto addFuel(long carId, double liters, double price, long odometer) throws ApiException {
        AddFuelRequestDto requestDto = new AddFuelRequestDto(liters, price, odometer);
        String json = gson.toJson(requestDto);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/cars/" + carId + "/fuel"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .timeout(TIMEOUT)
                .build();
        
        return executeRequest(request, FuelEntryDto.class);
    }

    public FuelStatsDto getFuelStats(long carId) throws ApiException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/cars/" + carId + "/fuel/stats"))
                .GET()
                .timeout(TIMEOUT)
                .build();
        
        return executeRequest(request, FuelStatsDto.class);
    }

    private <T> T executeRequest(HttpRequest request, Class<T> responseType) throws ApiException {
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return handleResponse(response, responseType);
        } catch (IOException e) {
            throw new ApiException("Connection error: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApiException("Request interrupted", e);
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseType) throws ApiException {
        int statusCode = response.statusCode();
        String body = response.body();
        
        if (statusCode >= 200 && statusCode < 300) {
            return gson.fromJson(body, responseType);
        } else {
            try {
                ErrorDto error = gson.fromJson(body, ErrorDto.class);
                throw new ApiException(error.getMessage(), statusCode, error);
            } catch (Exception e) {
                if (e instanceof ApiException) {
                    throw e;
                }
                throw new ApiException("HTTP " + statusCode + ": " + body, statusCode);
            }
        }
    }

    public static class ApiException extends Exception {
        
        private final int statusCode;
        private final ErrorDto error;

        public ApiException(String message, Throwable cause) {
            super(message, cause);
            this.statusCode = 0;
            this.error = null;
        }

        public ApiException(String message, int statusCode) {
            super(message);
            this.statusCode = statusCode;
            this.error = null;
        }

        public ApiException(String message, int statusCode, ErrorDto error) {
            super(message);
            this.statusCode = statusCode;
            this.error = error;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public ErrorDto getError() {
            return error;
        }
    }
}
