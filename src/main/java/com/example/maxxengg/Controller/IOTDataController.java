package com.example.maxxengg.Controller;

import com.example.maxxengg.Model.ErrorResponse;
import com.example.maxxengg.Model.IOTData;
import com.example.maxxengg.Repository.IOTDataRepository;
import com.example.maxxengg.Service.interfaces.IOTDataService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/data")
public class IOTDataController {

    @Autowired
    IOTDataRepository iotDataRepository;

    @Autowired
    IOTDataService iotDataService;

    @GetMapping("/getData")
    public ResponseEntity<?> getData() {
        try {
            List<IOTData> iotDataFromDB = iotDataRepository.findAllByOrderByIdDesc();

            if (iotDataFromDB.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(iotDataFromDB);
            }

            return ResponseEntity.ok(iotDataFromDB);
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorResponse errorResponse = new ErrorResponse("Failed to fetch data", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/getLatestRecord")
    public ResponseEntity<?> getLatestRecord() {
        try {
            List<IOTData> latestRecord = iotDataRepository.findTopByOrderByIdDesc();
            if (latestRecord.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(latestRecord);
            }
            return ResponseEntity.ok(latestRecord);
        } catch (Exception ex) {

            ex.printStackTrace();

            ErrorResponse errorResponse = new ErrorResponse("Failed to fetch data", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @GetMapping("/daily-consumption")
    public ResponseEntity<?> getDailyConsumption(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        try {
            LocalDate now = LocalDate.now();
            int currentYear = year != null ? year : now.getYear();
            int currentMonth = month != null ? month : now.getMonthValue();
    
            List<Map<String, Object>> consumptionData = iotDataService.getDailyConsumption(currentYear, currentMonth);
    
            if (consumptionData.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(Map.of("message", "No data found for the given year and month."));
            }
    
            return ResponseEntity.ok(consumptionData); 
        } catch (Exception e) {
            
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An error occurred while processing the request.", "details", e.getMessage()));
        }
    }
    
}
