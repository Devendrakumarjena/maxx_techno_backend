package com.example.maxxengg.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.maxxengg.Model.Device;
import com.example.maxxengg.Service.DeviceService;

@RestController
@RequestMapping("/api/devices")
@CrossOrigin("https://maxxtechnosolutions.com")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @PostMapping("/add")
    public ResponseEntity<Device> addDevice(@RequestBody Device device) {
        Device newDevice = deviceService.addDevice(device);
        return ResponseEntity.ok(newDevice);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Device>> getAllDevices() {
        List<Device> devices = deviceService.getAllDevices();
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/getByPlant/{plantId}")
    public ResponseEntity<List<Device>> getDevicesByPlant(@PathVariable Integer plantId) {
        List<Device> devices = deviceService.getDevicesByPlant(plantId);
        return ResponseEntity.ok(devices);
    }
}
