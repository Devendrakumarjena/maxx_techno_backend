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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;

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
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "No data available"));
            }

            return ResponseEntity.ok(iotDataFromDB);
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorResponse errorResponse = new ErrorResponse("Failed to fetch data", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/sendLiveData")
    public ResponseEntity<?> sendLiveData() {
        try {
            List<IOTData> iotDataFromDB = iotDataRepository.findAllByOrderByIdDesc();
            if (iotDataFromDB.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "No data available"));
            }

            // Transform the data into the desired format
            List<Map<String, Object>> formattedData = iotDataFromDB.stream().map(data -> {
                Map<String, Object> formattedEntry = new LinkedHashMap<>();
                formattedEntry.put("imie", data.getImie());
                formattedEntry.put("date", data.getDate());

                // Create a nested object for other data
                Map<String, Object> iotLiveData = new LinkedHashMap<>();
                iotLiveData.put("pv1Voltage", data.getPv1Voltage());
                iotLiveData.put("pv1Current", data.getPv1Current());
                iotLiveData.put("pv2Voltage", data.getPv2Voltage());
                iotLiveData.put("pv2Current", data.getPv2Current());
                iotLiveData.put("pv1Power", data.getPv1Power());
                iotLiveData.put("pv2Power", data.getPv2Power());
                iotLiveData.put("outputActivePower", data.getOutputActivePower());
                iotLiveData.put("outputReactivePower", data.getOutputReactivePower());
                iotLiveData.put("gridFrequency", data.getGridFrequency());
                iotLiveData.put("totalProductionHighByte", data.getTotalProductionHighByte());
                iotLiveData.put("totalProductionLowBytes", data.getTotalProductionLowBytes());
                iotLiveData.put("todayProduction", data.getTodayProduction());
                iotLiveData.put("todayGenerationTime", data.getTodayGenerationTime());
                iotLiveData.put("inverterModuleTemperature", data.getInverterModuleTemperature());
                iotLiveData.put("inverterInnerTemperature", data.getInverterInnerTemperature());
                iotLiveData.put("inverterBusVoltage", data.getInverterBusVoltage());
                formattedEntry.put("iotLiveData", iotLiveData);
                return formattedEntry;
            }).collect(Collectors.toList());

            messagingTemplate.convertAndSend("/topic/liveData", formattedData);
            return ResponseEntity.ok(formattedData); // Return the formatted data
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorResponse errorResponse = new ErrorResponse("Failed to send live data", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/getLatestRecord")
    public ResponseEntity<?> getLatestRecord() {
        try {
            List<IOTData> latestRecord = iotDataRepository.findTopByOrderByIdDesc();
            if (latestRecord.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "No data available"));
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
            // Get the current date and determine year/month
            LocalDate now = LocalDate.now();
            int currentYear = year != null ? year : now.getYear();
            int currentMonth = month != null ? month : now.getMonthValue();
    
            // Fetch consumption data
            List<Map<String, Object>> consumptionData = iotDataService.getDailyConsumption(currentYear, currentMonth);
    
            // Check if data is empty
            if (consumptionData.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                    "status", "no_data",
                    "message", "No data found for the given year and month."
                ));
            }
    
            // Return the data if available
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", consumptionData
            ));
        } catch (Exception e) {
            e.printStackTrace();
    
            // Handle exceptions gracefully
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "status", "error",
                        "message", "An error occurred while processing the request.",
                        "details", e.getMessage()
                    ));
        }
    }
    
}
