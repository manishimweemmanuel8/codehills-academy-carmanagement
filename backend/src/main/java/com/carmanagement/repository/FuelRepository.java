package com.carmanagement.repository;

import com.carmanagement.model.FuelEntry;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class FuelRepository implements FuelRepositoryInterface {
    
    private final Map<Long, FuelEntry> storage = new ConcurrentHashMap<>();
    private final Map<Long, List<Long>> carIdIndex = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    @Override
    public FuelEntry save(FuelEntry entry) {
        if (entry.getId() == null) {
            entry.setId(idGenerator.incrementAndGet());
        }
        
        Long entryId = entry.getId();
        Long carId = entry.getCarId();
        
        FuelEntry existingEntry = storage.get(entryId);
        boolean isUpdate = existingEntry != null;
        
        if (isUpdate) {
            Long oldCarId = existingEntry.getCarId();
            if (!oldCarId.equals(carId)) {
                List<Long> oldIndex = carIdIndex.get(oldCarId);
                if (oldIndex != null) {
                    oldIndex.remove(entryId);
                    if (oldIndex.isEmpty()) {
                        carIdIndex.remove(oldCarId);
                    }
                }
            } else {
                List<Long> currentIndex = carIdIndex.get(carId);
                if (currentIndex != null) {
                    currentIndex.remove(entryId);
                }
            }
        }
        
        storage.put(entryId, entry);
        carIdIndex.computeIfAbsent(carId, k -> new CopyOnWriteArrayList<>()).add(entryId);
        
        return entry;
    }

    @Override
    public List<FuelEntry> findByCarId(Long carId) {
        List<Long> entryIds = carIdIndex.get(carId);
        if (entryIds == null || entryIds.isEmpty()) {
            return new java.util.ArrayList<>();
        }
        
        List<FuelEntry> entries = entryIds.stream()
                .map(storage::get)
                .filter(entry -> entry != null && carId.equals(entry.getCarId()))
                .sorted(Comparator.comparing(FuelEntry::getOdometer))
                .collect(Collectors.toList());
        
        List<Long> staleIds = entryIds.stream()
                .filter(id -> storage.get(id) == null || !carId.equals(storage.get(id).getCarId()))
                .collect(Collectors.toList());
        
        if (!staleIds.isEmpty()) {
            entryIds.removeAll(staleIds);
            if (entryIds.isEmpty()) {
                carIdIndex.remove(carId);
            }
        }
        
        return entries;
    }

    @Override
    public Long getMaxOdometerByCarId(Long carId) {
        List<Long> entryIds = carIdIndex.get(carId);
        if (entryIds == null || entryIds.isEmpty()) {
            return null;
        }
        
        Long maxOdometer = entryIds.stream()
                .map(storage::get)
                .filter(entry -> entry != null && carId.equals(entry.getCarId()))
                .map(FuelEntry::getOdometer)
                .max(Long::compareTo)
                .orElse(null);
        
        List<Long> staleIds = entryIds.stream()
                .filter(id -> storage.get(id) == null || !carId.equals(storage.get(id).getCarId()))
                .collect(Collectors.toList());
        
        if (!staleIds.isEmpty()) {
            entryIds.removeAll(staleIds);
            if (entryIds.isEmpty()) {
                carIdIndex.remove(carId);
            }
        }
        
        return maxOdometer;
    }

    @Override
    public long count() {
        return storage.size();
    }

    @Override
    public void clear() {
        storage.clear();
        carIdIndex.clear();
        idGenerator.set(0);
    }
}

