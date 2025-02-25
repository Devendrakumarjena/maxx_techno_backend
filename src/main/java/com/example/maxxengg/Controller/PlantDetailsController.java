package com.example.maxxengg.Controller;

import com.example.maxxengg.Model.Plantdetails;

import com.example.maxxengg.Service.PlantDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/plants")
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular requests
public class PlantDetailsController {
    @Autowired
    private PlantDetailsService plantDetailsService;

    @GetMapping("/getAllPlants")
    public ResponseEntity<?> getAllPlants() {
        List<Plantdetails> plants = plantDetailsService.getAllPlants();
        if (plants.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(Map.of("message", "No plant details available"));
        }
        return ResponseEntity.ok(plants);
    }

    @PostMapping("/addPlant")
    public ResponseEntity<?> addNewPlant(@RequestBody Plantdetails plant) {
        try {
            Plantdetails savedPlant = plantDetailsService.addPlant(plant);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Plant added successfully", "data", savedPlant));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Failed to add plant", "error", e.getMessage()));
        }
    }

}
