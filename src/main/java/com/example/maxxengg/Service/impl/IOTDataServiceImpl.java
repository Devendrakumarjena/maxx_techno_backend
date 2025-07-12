package com.example.maxxengg.Service.impl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.maxxengg.Model.IOTData;
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
                double totalConsumptionMW = totalConsumptionWatts / 100; // Convert W to MW
                updatedRecord.put("totalConsumption", totalConsumptionMW);
            }

            return updatedRecord;
        }).toList();
    }

    @Override
    public List<Object[]> getHourlyConsumptionByDate(String date) {
        return iotDataRepository.findHourlyConsumptionByDate(date);
    }

    @Override
    public Optional<IOTData> getLatestDataByImie(String imie) {
        // TODO Auto-generated method stub
        return Optional.ofNullable(iotDataRepository.findLatestDataByImie(imie));
    }

    @Override
    public Boolean findIssue(String inputDateTimeStr){
        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm.ss");
        try {
            // Parse the input time string
            LocalDateTime inputTime = LocalDateTime.parse(inputDateTimeStr, FORMATTER);
            ZoneId indiaZone = ZoneId.of("Asia/Kolkata");
            LocalDateTime now = LocalDateTime.now(indiaZone);
//            LocalDateTime now = LocalDateTime.now();
            System.out.println("Indian time "+now);
            // Condition 1: Time between 7AM and 6PM (i.e., 07:00 to 18:00)
            int hour = inputTime.getHour();
            boolean isWithinDaytime = hour >= 6 && hour < 18;

            // Condition 2: Time difference > 2 minutes
            long diffInSeconds = Math.abs(Duration.between(inputTime, now).getSeconds());
            boolean isMoreThanTwoMinutes = diffInSeconds > 600;

            // Return true only if both conditions are satisfied
            return isWithinDaytime && isMoreThanTwoMinutes;

        } catch (Exception e) {
            // Handle parse errors gracefully
            System.err.println("Invalid datetime format: " + inputDateTimeStr);
            return false;
        }

    }

}