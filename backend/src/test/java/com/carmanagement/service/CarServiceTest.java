package com.carmanagement.service;

import com.carmanagement.dto.request.CreateCarRequest;
import com.carmanagement.dto.response.CarResponse;
import com.carmanagement.exception.ValidationException;
import com.carmanagement.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for CarService.
class CarServiceTest {
    
    private CarService carService;
    private CarRepository carRepository;

    @BeforeEach
    void setUp() {
        carRepository = new CarRepository();
        carService = new CarService(carRepository);
    }

    @Test
    @DisplayName("Should create car successfully")
    void shouldCreateCarSuccessfully() {
        CreateCarRequest request = new CreateCarRequest("Toyota", "Corolla", 2020);
        
        CarResponse response = carService.createCar(request);
        
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("Toyota", response.getBrand());
        assertEquals("Corolla", response.getModel());
        assertEquals(2020, response.getYear());
        assertNotNull(response.getCreatedAt());
    }

    @Test
    @DisplayName("Should trim whitespace from brand and model")
    void shouldTrimWhitespace() {
        CreateCarRequest request = new CreateCarRequest("  Toyota  ", "  Corolla  ", 2020);
        
        CarResponse response = carService.createCar(request);
        
        assertEquals("Toyota", response.getBrand());
        assertEquals("Corolla", response.getModel());
    }

    @Test
    @DisplayName("Should throw ValidationException for missing brand")
    void shouldThrowForMissingBrand() {
        CreateCarRequest request = new CreateCarRequest(null, "Corolla", 2020);
        
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> carService.createCar(request)
        );
        
        assertTrue(exception.hasFieldErrors());
        assertTrue(exception.getFieldErrors().containsKey("brand"));
    }

    @Test
    @DisplayName("Should throw ValidationException for empty brand")
    void shouldThrowForEmptyBrand() {
        CreateCarRequest request = new CreateCarRequest("  ", "Corolla", 2020);
        
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> carService.createCar(request)
        );
        
        assertTrue(exception.getFieldErrors().containsKey("brand"));
    }

    @Test
    @DisplayName("Should throw ValidationException for missing model")
    void shouldThrowForMissingModel() {
        CreateCarRequest request = new CreateCarRequest("Toyota", null, 2020);
        
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> carService.createCar(request)
        );
        
        assertTrue(exception.getFieldErrors().containsKey("model"));
    }

    @Test
    @DisplayName("Should throw ValidationException for missing year")
    void shouldThrowForMissingYear() {
        CreateCarRequest request = new CreateCarRequest("Toyota", "Corolla", null);
        
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> carService.createCar(request)
        );
        
        assertTrue(exception.getFieldErrors().containsKey("year"));
    }

    @Test
    @DisplayName("Should throw ValidationException for year before 1900")
    void shouldThrowForYearBefore1900() {
        CreateCarRequest request = new CreateCarRequest("Toyota", "Corolla", 1899);
        
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> carService.createCar(request)
        );
        
        assertTrue(exception.getFieldErrors().containsKey("year"));
    }

    @Test
    @DisplayName("Should throw ValidationException for future year")
    void shouldThrowForFutureYear() {
        CreateCarRequest request = new CreateCarRequest("Toyota", "Corolla", 2100);
        
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> carService.createCar(request)
        );
        
        assertTrue(exception.getFieldErrors().containsKey("year"));
    }

    @Test
    @DisplayName("Should collect multiple validation errors")
    void shouldCollectMultipleErrors() {
        CreateCarRequest request = new CreateCarRequest(null, null, null);
        
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> carService.createCar(request)
        );
        
        assertEquals(3, exception.getFieldErrors().size());
    }

    @Test
    @DisplayName("Should get all cars")
    void shouldGetAllCars() {
        carService.createCar(new CreateCarRequest("Toyota", "Corolla", 2020));
        carService.createCar(new CreateCarRequest("Honda", "Civic", 2021));
        
        List<CarResponse> cars = carService.getAllCars();
        
        assertEquals(2, cars.size());
    }

    @Test
    @DisplayName("Should return empty list when no cars")
    void shouldReturnEmptyListWhenNoCars() {
        List<CarResponse> cars = carService.getAllCars();
        
        assertTrue(cars.isEmpty());
    }

    @Test
    @DisplayName("Should check if car exists")
    void shouldCheckIfCarExists() {
        CarResponse created = carService.createCar(new CreateCarRequest("Toyota", "Corolla", 2020));
        
        assertTrue(carService.existsById(created.getId()));
        assertFalse(carService.existsById(999L));
    }
}

