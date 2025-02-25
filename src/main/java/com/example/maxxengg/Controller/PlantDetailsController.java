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

    @PostMapping
    public Plantdetails addPlant(@RequestBody Plantdetails plant) {
        return plantDetailsService.addPlant(plant);
    }

    @GetMapping("/getAllPlants")
    public ResponseEntity<?> getAllPlants() {
        List<Plantdetails> plants = plantDetailsService.getAllPlants();
        if (plants.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(Map.of("message", "No plant details available"));
        }
        return ResponseEntity.ok(plants);
    }

}
