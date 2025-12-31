package com.carmanagement.repository;

import com.carmanagement.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for CarRepository.
class CarRepositoryTest {
    
    private CarRepository repository;

    @BeforeEach
    void setUp() {
        repository = new CarRepository();
    }

    @Test
    @DisplayName("Should save car and assign ID")
    void shouldSaveCarAndAssignId() {
        Car car = new Car("Toyota", "Corolla", 2020);
        
        Car savedCar = repository.save(car);
        
        assertNotNull(savedCar.getId());
        assertEquals(1L, savedCar.getId());
        assertEquals("Toyota", savedCar.getBrand());
    }

    @Test
    @DisplayName("Should increment IDs for multiple cars")
    void shouldIncrementIds() {
        Car car1 = new Car("Toyota", "Corolla", 2020);
        Car car2 = new Car("Honda", "Civic", 2021);
        
        repository.save(car1);
        repository.save(car2);
        
        assertEquals(1L, car1.getId());
        assertEquals(2L, car2.getId());
    }

    @Test
    @DisplayName("Should find all cars")
    void shouldFindAllCars() {
        repository.save(new Car("Toyota", "Corolla", 2020));
        repository.save(new Car("Honda", "Civic", 2021));
        repository.save(new Car("Ford", "Focus", 2019));
        
        List<Car> cars = repository.findAll();
        
        assertEquals(3, cars.size());
    }

    @Test
    @DisplayName("Should return empty list when no cars")
    void shouldReturnEmptyListWhenNoCars() {
        List<Car> cars = repository.findAll();
        
        assertTrue(cars.isEmpty());
    }

    @Test
    @DisplayName("Should check if car exists by ID")
    void shouldCheckIfCarExistsById() {
        repository.save(new Car("Toyota", "Corolla", 2020));
        
        assertTrue(repository.existsById(1L));
        assertFalse(repository.existsById(999L));
    }

    @Test
    @DisplayName("Should delete car by ID")
    void shouldDeleteCarById() {
        repository.save(new Car("Toyota", "Corolla", 2020));
        
        boolean deleted = repository.deleteById(1L);
        
        assertTrue(deleted);
        assertFalse(repository.existsById(1L));
    }

    @Test
    @DisplayName("Should return false when deleting non-existent car")
    void shouldReturnFalseWhenDeletingNonExistent() {
        boolean deleted = repository.deleteById(999L);
        
        assertFalse(deleted);
    }

    @Test
    @DisplayName("Should count cars correctly")
    void shouldCountCars() {
        assertEquals(0, repository.count());
        
        repository.save(new Car("Toyota", "Corolla", 2020));
        repository.save(new Car("Honda", "Civic", 2021));
        
        assertEquals(2, repository.count());
    }

    @Test
    @DisplayName("Should clear all cars")
    void shouldClearAllCars() {
        repository.save(new Car("Toyota", "Corolla", 2020));
        repository.save(new Car("Honda", "Civic", 2021));
        
        repository.clear();
        
        assertEquals(0, repository.count());
        assertTrue(repository.findAll().isEmpty());
    }
}

