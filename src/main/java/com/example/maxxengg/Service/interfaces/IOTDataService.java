package com.example.maxxengg.Service.interfaces;

import com.example.maxxengg.Repository.IOTDataRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IOTDataService {
    List<Map<String, Object>> getDailyConsumption(int year, int month);
    List<Object[]> getHourlyConsumptionByDate(String date);


}
