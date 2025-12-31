package com.carmanagement.repository;

import com.carmanagement.model.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class CarRepository implements CarRepositoryInterface {
    
    private final Map<Long, Car> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    @Override
    public Car save(Car car) {
        if (car.getId() == null) {
            car.setId(idGenerator.incrementAndGet());
        }
        storage.put(car.getId(), car);
        return car;
    }

    @Override
    public List<Car> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return storage.remove(id) != null;
    }

    @Override
    public long count() {
        return storage.size();
    }

    @Override
    public void clear() {
        storage.clear();
        idGenerator.set(0);
    }
}

