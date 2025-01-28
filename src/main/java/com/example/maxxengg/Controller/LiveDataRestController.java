package com.example.maxxengg.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.maxxengg.Model.IOTData;
import com.example.maxxengg.Repository.IOTDataRepository;

import java.util.List;

@RestController
@RequestMapping("/liveData")
public class LiveDataRestController {

    @Autowired
    private IOTDataRepository iotDataRepository;

    // REST API to get live data
    @GetMapping("/getLiveData")
    public List<IOTData> getLiveData() {
        return iotDataRepository.findAllByOrderByIdDesc();
    }
}
