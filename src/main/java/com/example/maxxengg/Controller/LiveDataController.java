package com.example.maxxengg.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.example.maxxengg.Model.IOTData;
import com.example.maxxengg.Repository.IOTDataRepository;

import java.util.List;

@Controller
public class LiveDataController {


    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private IOTDataRepository iotDataRepository;

    // Periodically fetch data and broadcast to WebSocket clients
    @Scheduled(fixedRate = 5000) // Every 5 seconds
    public void broadcastLiveData() {
        List<IOTData> liveData = iotDataRepository.findAllByOrderByIdDesc();
        messagingTemplate.convertAndSend("/topic/liveData", liveData);
    }

    // Handle WebSocket client requests
    @MessageMapping("/getLiveData")
    public void handleRequest() {
        List<IOTData> liveData = iotDataRepository.findAllByOrderByIdDesc();
        messagingTemplate.convertAndSend("/topic/liveData", liveData);
    }
}
