package com.example.maxxengg.Service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.maxxengg.Repository.IOTDataRepository;
import com.example.maxxengg.Service.interfaces.IOTDataService;

@Service
public class IOTDataServiceImpl implements IOTDataService {

    private final IOTDataRepository iotDataRepository;

    public IOTDataServiceImpl(IOTDataRepository iotDataRepository) {
        this.iotDataRepository = iotDataRepository;
    }

    @Override
    public List<Map<String, Object>> getDailyConsumption(int year, int month) {
        // Fetch data in watts from the repository
        List<Map<String, Object>> rawData = iotDataRepository.findDailyConsumption(year, month);

        // Convert totalConsumption to megawatts in the service layer
        return rawData.stream().map(record -> {
            Map<String, Object> updatedRecord = new HashMap<>(record);

            // Convert totalConsumption from watts to megawatts
            if (record.containsKey("totalConsumption")) {
                double totalConsumptionWatts = ((Number) record.get("totalConsumption")).doubleValue();
                double totalConsumptionMW = totalConsumptionWatts / 1_000_000; // Convert W to MW
                updatedRecord.put("totalConsumption", totalConsumptionMW);
            }

            return updatedRecord;
        }).toList();
    }

}
