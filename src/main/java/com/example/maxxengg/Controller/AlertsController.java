package com.example.maxxengg.Controller;
import com.example.maxxengg.Model.Alert;
import com.example.maxxengg.Repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@CrossOrigin("https://maxxtechnosolutions.com")
public class AlertsController {

    @Autowired
    private AlertRepository alertRepository;

    // GET /api/alerts?imei=123456789
    @GetMapping
    public List<Alert> getAlertsByImei(@RequestParam String imei) {
        return alertRepository.findByImeiOrderByCreatedAtDesc(imei);
    }
}
