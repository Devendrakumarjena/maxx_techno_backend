package com.example.maxxengg.Controller;

import com.example.maxxengg.Model.ErrorResponse;
import com.example.maxxengg.Model.IOTData;
import com.example.maxxengg.Repository.IOTDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/data")
public class IOTDataController {

    @Autowired
    IOTDataRepository iotDataRepository;


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

}
