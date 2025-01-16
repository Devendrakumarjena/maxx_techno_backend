package com.example.maxxengg.Repository;

import com.example.maxxengg.Model.IOTData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface IOTDataRepository extends JpaRepository<IOTData, Integer> {
    List<IOTData> findAllByOrderByIdDesc();

    List<IOTData> findTopByOrderByIdDesc();

    @Query("SELECT DATE(TO_DATE(i.date, 'YYYY.MM.DD.HH24.MI.SS')) AS day, " +
    "SUM(i.todayProduction) AS totalConsumption " +
    "FROM IOTData i " +
    "WHERE EXTRACT(MONTH FROM TO_DATE(i.date, 'YYYY.MM.DD.HH24.MI.SS')) = :month " +
    "AND EXTRACT(YEAR FROM TO_DATE(i.date, 'YYYY.MM.DD.HH24.MI.SS')) = :year " +
    "GROUP BY DATE(TO_DATE(i.date, 'YYYY.MM.DD.HH24.MI.SS')) " +
    "ORDER BY day")
    List<Map<String, Object>> findDailyConsumption(@Param("year") int year, @Param("month") int month);

      // Fetch hourly consumption for a specific date
      @Query("SELECT " +
      "FUNCTION('HOUR', i.timestamp) AS hour, " +
      "SUM(i.outputActivePower) AS totalPower " +
      "FROM IoTData i " +
      "WHERE DATE(i.timestamp) = :date " +
      "GROUP BY FUNCTION('HOUR', i.timestamp) " +
      "ORDER BY hour")
      List<Object[]> findHourlyConsumptionByDate(java.time.LocalDate date);

}
