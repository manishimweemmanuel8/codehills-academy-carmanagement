package com.carmanagement.repository;

import com.carmanagement.model.Car;

import java.util.List;


public interface CarRepositoryInterface {
    

    Car save(Car car);

    List<Car> findAll();

    boolean existsById(Long id);

    boolean deleteById(Long id);

    long count();

    void clear();
}

