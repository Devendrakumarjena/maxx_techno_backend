package com.example.maxxengg.Repository;

import com.example.maxxengg.Model.IOTData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IOTDataRepository extends JpaRepository<IOTData, Integer> {
    List<IOTData> findAllByOrderByIdDesc();

    List<IOTData> findTopByOrderByIdDesc();
}
