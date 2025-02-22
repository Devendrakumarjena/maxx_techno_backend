package com.example.maxxengg.Controller;

import com.example.maxxengg.Model.ErrorResponse;
import com.example.maxxengg.Model.IOTData;
import com.example.maxxengg.Repository.IOTDataRepository;
import com.example.maxxengg.Service.interfaces.IOTDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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

    @GetMapping("/getTodaysData")
    public ResponseEntity<?> getTodaysData(){
        LocalDate today = LocalDate.now();
        return ResponseEntity.ok(iotDataRepository.findAllByDate(today));
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

//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;

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

//            messagingTemplate.convertAndSend("/topic/liveData", formattedData);
            return ResponseEntity.ok(formattedData); // Return the formatted data
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorResponse errorResponse = new ErrorResponse("Failed to send live data", ex.getMessage());
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

    @GetMapping("/hourlyConsumption")
    public ResponseEntity<?> getHourlyConsumption(@RequestParam String date) {
        try {
            // Parse the date
            LocalDate today = LocalDate.now();
            log.info("today "+today);

            // Fetch hourly consumption data
            List<Object[]> results = iotDataService.getHourlyConsumptionByDate(date);
            log.info("data "+results);
            // If no data is found, return an appropriate message
            if (results.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                        "status", "no_data",
                        "message", "No data found for the given date."
                ));
            }

            // Format the response
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("date", date);
            response.put("data", results);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "status", "error",
                    "message", "An error occurred while processing the request.",
                    "details", e.getMessage()
            ));
        }
    }

    @GetMapping("/getYesterdayDetails")
    public ResponseEntity<?> getYesterdayDetails() {
        try {
            LocalDate today=LocalDate.now();
            String yesterday=String.valueOf(today.getYear());
            if(today.getMonthValue()<10){
                yesterday+=".0"+today.getMonthValue();
            }
            else{
                yesterday+="."+today.getMonthValue();
            }
            if(today.getDayOfMonth()<10){
                yesterday+=".0"+(today.getDayOfMonth()-1);
            }
            else{
                yesterday+="."+(today.getDayOfMonth()-1);
            }
//            String yesterday=String.valueOf(today.getYear())+"."+String.valueOf(today.getMonthValue())+"."+String.valueOf(today.getDayOfMonth()-1);
            log.info("yesterday "+yesterday);
            List<IOTData> latestRecord = iotDataRepository.findYesterdayGenerationDetails(yesterday);
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

    @GetMapping("/getMonthlyAndYearlyData")
    public ResponseEntity<?> getMonthlyData(){
        try {
            // Parse the date
            LocalDate today = LocalDate.now();
            log.info("today "+today);

            // Fetch hourly consumption data
            List<Integer> monthlyResults = iotDataRepository.findMonthlyData(String.valueOf(today.getMonthValue()),String.valueOf(today.getYear()));
            List<Integer> yearlyResults = iotDataRepository.findYearlyData(String.valueOf(today.getYear()));
            Integer monthlytotal=0;
            Integer yearlyTotal=0;
            for(Integer i : monthlyResults){
                monthlytotal+=i;
            }
            for(Integer j : yearlyResults){
                yearlyTotal+=j;
            }
            List<Integer> results=new ArrayList<>();
            results.add(monthlytotal);
            results.add(yearlyTotal);
            results.add(monthlyResults.size());
            results.add(yearlyResults.size());


            log.info("data "+results);
            // If no data is found, return an appropriate message
            if (results.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                        "status", "no_data",
                        "message", "No data found for the given date."
                ));
            }

            // Format the response
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", results);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "status", "error",
                    "message", "An error occurred while processing the request.",
                    "details", e.getMessage()
            ));
        }
    }



}
