package com.example.maxxengg.Service;

import com.example.maxxengg.Model.Plantdetails;
import com.example.maxxengg.Repository.PlantDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantDetailsService {
    @Autowired
    private PlantDetailsRepository plantDetailsRepository;

    public Plantdetails addPlant(Plantdetails plant) {
        return plantDetailsRepository.save(plant);
    }

    public List<Plantdetails> getAllPlants() {
        return plantDetailsRepository.findAll();
    }
}
