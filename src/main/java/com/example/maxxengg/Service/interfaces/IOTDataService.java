package com.example.maxxengg.Service.interfaces;

import java.util.List;
import java.util.Map;

public interface IOTDataService {
    List<Map<String, Object>> getDailyConsumption(int year, int month);
}
