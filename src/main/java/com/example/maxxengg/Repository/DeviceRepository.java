package com.example.maxxengg.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.maxxengg.Model.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {
    List<Device> findByPlantId(Integer plantId);
}
