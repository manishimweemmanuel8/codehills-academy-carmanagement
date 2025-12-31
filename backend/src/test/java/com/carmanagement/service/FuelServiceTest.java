package com.carmanagement.service;

import com.carmanagement.dto.request.AddFuelRequest;
import com.carmanagement.dto.response.FuelEntryResponse;
import com.carmanagement.dto.response.FuelStatsResponse;
import com.carmanagement.exception.NotFoundException;
import com.carmanagement.exception.ValidationException;
import com.carmanagement.model.Car;
import com.carmanagement.repository.CarRepository;
import com.carmanagement.repository.FuelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for FuelService.
class FuelServiceTest {
    
    private FuelService fuelService;
    private CarRepository carRepository;
    private FuelRepository fuelRepository;
    private Long testCarId;

    @BeforeEach
    void setUp() {
        carRepository = new CarRepository();
        fuelRepository = new FuelRepository();
        fuelService = new FuelService(fuelRepository, carRepository);
        
        // Create a test car.
        Car testCar = new Car("Toyota", "Corolla", 2020);
        carRepository.save(testCar);
        testCarId = testCar.getId();
    }

    @Test
    @DisplayName("Should add fuel entry successfully")
    void shouldAddFuelEntrySuccessfully() {
        AddFuelRequest request = new AddFuelRequest(40.0, 52.50, 45000L);
        
        FuelEntryResponse response = fuelService.addFuelEntry(testCarId, request);
        
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(testCarId, response.getCarId());
        assertEquals(40.0, response.getLiters());
        assertEquals(52.50, response.getPrice());
        assertEquals(45000L, response.getOdometer());
    }

    @Test
    @DisplayName("Should throw NotFoundException for non-existent car")
    void shouldThrowForNonExistentCar() {
        AddFuelRequest request = new AddFuelRequest(40.0, 52.50, 45000L);
        
        NotFoundException exception = assertThrows(
            NotFoundException.class,
            () -> fuelService.addFuelEntry(999L, request)
        );
        
        assertTrue(exception.getMessage().contains("999"));
    }

    @Test
    @DisplayName("Should throw ValidationException for missing liters")
    void shouldThrowForMissingLiters() {
        AddFuelRequest request = new AddFuelRequest(null, 52.50, 45000L);
        
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> fuelService.addFuelEntry(testCarId, request)
        );
        
        assertTrue(exception.getFieldErrors().containsKey("liters"));
    }

    @Test
    @DisplayName("Should throw ValidationException for zero liters")
    void shouldThrowForZeroLiters() {
        AddFuelRequest request = new AddFuelRequest(0.0, 52.50, 45000L);
        
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> fuelService.addFuelEntry(testCarId, request)
        );
        
        assertTrue(exception.getFieldErrors().containsKey("liters"));
    }

    @Test
    @DisplayName("Should throw ValidationException for negative liters")
    void shouldThrowForNegativeLiters() {
        AddFuelRequest request = new AddFuelRequest(-10.0, 52.50, 45000L);
        
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> fuelService.addFuelEntry(testCarId, request)
        );
        
        assertTrue(exception.getFieldErrors().containsKey("liters"));
    }

    @Test
    @DisplayName("Should throw ValidationException for missing price")
    void shouldThrowForMissingPrice() {
        AddFuelRequest request = new AddFuelRequest(40.0, null, 45000L);
        
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> fuelService.addFuelEntry(testCarId, request)
        );
        
        assertTrue(exception.getFieldErrors().containsKey("price"));
    }

    @Test
    @DisplayName("Should throw ValidationException for negative price")
    void shouldThrowForNegativePrice() {
        AddFuelRequest request = new AddFuelRequest(40.0, -10.0, 45000L);
        
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> fuelService.addFuelEntry(testCarId, request)
        );
        
        assertTrue(exception.getFieldErrors().containsKey("price"));
    }

    @Test
    @DisplayName("Should allow zero price (free fuel)")
    void shouldAllowZeroPrice() {
        AddFuelRequest request = new AddFuelRequest(40.0, 0.0, 45000L);
        
        FuelEntryResponse response = fuelService.addFuelEntry(testCarId, request);
        
        assertEquals(0.0, response.getPrice());
    }

    @Test
    @DisplayName("Should throw ValidationException for missing odometer")
    void shouldThrowForMissingOdometer() {
        AddFuelRequest request = new AddFuelRequest(40.0, 52.50, null);
        
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> fuelService.addFuelEntry(testCarId, request)
        );
        
        assertTrue(exception.getFieldErrors().containsKey("odometer"));
    }

    @Test
    @DisplayName("Should throw ValidationException for zero odometer")
    void shouldThrowForZeroOdometer() {
        AddFuelRequest request = new AddFuelRequest(40.0, 52.50, 0L);
        
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> fuelService.addFuelEntry(testCarId, request)
        );
        
        assertTrue(exception.getFieldErrors().containsKey("odometer"));
    }

    @Test
    @DisplayName("Should return empty stats when no entries")
    void shouldReturnEmptyStatsWhenNoEntries() {
        FuelStatsResponse stats = fuelService.getFuelStats(testCarId);
        
        assertEquals(0.0, stats.getTotalFuel());
        assertEquals(0.0, stats.getTotalCost());
        assertEquals(0.0, stats.getAverageConsumption());
        assertEquals(0, stats.getEntryCount());
    }

    @Test
    @DisplayName("Should calculate total fuel and cost")
    void shouldCalculateTotalFuelAndCost() {
        fuelService.addFuelEntry(testCarId, new AddFuelRequest(30.0, 40.0, 44000L));
        fuelService.addFuelEntry(testCarId, new AddFuelRequest(35.0, 45.0, 45000L));
        fuelService.addFuelEntry(testCarId, new AddFuelRequest(40.0, 50.0, 46000L));
        
        FuelStatsResponse stats = fuelService.getFuelStats(testCarId);
        
        assertEquals(105.0, stats.getTotalFuel());
        assertEquals(135.0, stats.getTotalCost());
        assertEquals(3, stats.getEntryCount());
    }

    @Test
    @DisplayName("Should calculate average consumption correctly")
    void shouldCalculateAverageConsumption() {
        // First fill: 40L at 40,000 km (baseline)
        fuelService.addFuelEntry(testCarId, new AddFuelRequest(40.0, 50.0, 40000L));
        // Second fill: 45L at 40,500 km (500 km driven, used 45L)
        fuelService.addFuelEntry(testCarId, new AddFuelRequest(45.0, 55.0, 40500L));
        // Third fill: 50L at 41,000 km (500 km driven, used 50L)
        fuelService.addFuelEntry(testCarId, new AddFuelRequest(50.0, 60.0, 41000L));
        
        // Total distance: 1000 km (from 40000 to 41000)
        // Fuel used (excluding first): 45 + 50 = 95L
        // Average: (95 / 1000) * 100 = 9.5 L/100km
        
        FuelStatsResponse stats = fuelService.getFuelStats(testCarId);
        
        assertEquals(9.5, stats.getAverageConsumption());
    }

    @Test
    @DisplayName("Should return zero consumption for single entry")
    void shouldReturnZeroConsumptionForSingleEntry() {
        fuelService.addFuelEntry(testCarId, new AddFuelRequest(40.0, 50.0, 40000L));
        
        FuelStatsResponse stats = fuelService.getFuelStats(testCarId);
        
        assertEquals(0.0, stats.getAverageConsumption());
        assertEquals(1, stats.getEntryCount());
    }

    @Test
    @DisplayName("Should throw NotFoundException when getting stats for non-existent car")
    void shouldThrowWhenGettingStatsForNonExistentCar() {
        assertThrows(
            NotFoundException.class,
            () -> fuelService.getFuelStats(999L)
        );
    }

    @Test
    @DisplayName("Should throw ValidationException when odometer decreases")
    void shouldThrowValidationExceptionWhenOdometerDecreases() {
        // First entry at 45000 km
        fuelService.addFuelEntry(testCarId, new AddFuelRequest(40.0, 50.0, 45000L));
        
        // Try to add entry with lower odometer reading
        AddFuelRequest request = new AddFuelRequest(35.0, 45.0, 44000L);
        
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> fuelService.addFuelEntry(testCarId, request)
        );
        
        assertTrue(exception.hasFieldErrors());
        assertTrue(exception.getFieldErrors().containsKey("odometer"));
        assertTrue(exception.getFieldErrors().get("odometer").contains("must be greater than or equal"));
    }

    @Test
    @DisplayName("Should return zero consumption when odometer does not increase")
    void shouldReturnZeroConsumptionWhenOdometerDoesNotIncrease() {
        // First entry at 40000 km
        fuelService.addFuelEntry(testCarId, new AddFuelRequest(40.0, 50.0, 40000L));
        // Second entry at same odometer (40000 km) - distance is 0
        fuelService.addFuelEntry(testCarId, new AddFuelRequest(45.0, 55.0, 40000L));
        
        FuelStatsResponse stats = fuelService.getFuelStats(testCarId);
        
        // Total fuel and cost should be calculated
        assertEquals(85.0, stats.getTotalFuel());
        assertEquals(105.0, stats.getTotalCost());
        assertEquals(2, stats.getEntryCount());
        // Consumption should be 0 because distance is 0
        assertEquals(0.0, stats.getAverageConsumption());
    }
}

