package com.example.maxxengg.Repository;

import com.example.maxxengg.Model.IOTData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IOTDataRepository extends JpaRepository<IOTData, Integer> {
    List<IOTData> findAllByOrderByIdDesc();

    List<IOTData> findTopByOrderByIdDesc();

    @Query("SELECT DATE(TO_DATE(i.date, 'YYYY.MM.DD.HH24.MI.SS')) AS day, " +
            "MAX(i.todayProduction) AS totalConsumption " +
            "FROM IOTData i " +
            "WHERE EXTRACT(MONTH FROM TO_DATE(i.date, 'YYYY.MM.DD.HH24.MI.SS')) = :month " +
            "AND EXTRACT(YEAR FROM TO_DATE(i.date, 'YYYY.MM.DD.HH24.MI.SS')) = :year " +
            "GROUP BY DATE(TO_DATE(i.date, 'YYYY.MM.DD.HH24.MI.SS')) " +
            "ORDER BY day")
    List<Map<String, Object>> findDailyConsumption(@Param("year") int year, @Param("month") int month);

    // Fetch hourly consumption for a specific date
    @Query(value="SELECT SUBSTRING(date FROM 12 FOR 2) AS hour,ROUND((MAX(today_production)-MIN(today_production))/100.0,2) AS totalPower FROM public.iotdata WHERE LEFT(date, 10) = :date GROUP BY SUBSTRING(date FROM 12 FOR 2)ORDER BY hour",nativeQuery = true)
    List<Object[]> findHourlyConsumptionByDate(String date);

    @Query(value="SELECT * FROM public.iotdata WHERE LEFT(date, 10) =  :date ORDER BY id desc limit 1",nativeQuery = true)
    List<IOTData> findYesterdayGenerationDetails(String date);

    @Query(value="SELECT * FROM public.iotdata WHERE LEFT(date, 10) = TO_CHAR(CURRENT_DATE-1, 'YYYY.MM.DD')",nativeQuery = true)
    List<IOTData> findAllByDate(@Param("currentDate") LocalDate currentDate);

    @Query(value="SELECT " +
            "MAX(i.todayProduction) AS totalConsumption " +
            "FROM IOTData i " +
            "WHERE EXTRACT(MONTH FROM TO_DATE(i.date, 'YYYY.MM.DD.HH24.MI.SS')) = :month " +
            "AND EXTRACT(YEAR FROM TO_DATE(i.date, 'YYYY.MM.DD.HH24.MI.SS')) = :year " +
            "GROUP BY DATE(TO_DATE(i.date, 'YYYY.MM.DD.HH24.MI.SS')) "
            )
    List<Integer> findMonthlyData(String month,String year);

    @Query(value="SELECT " +
            "MAX(i.todayProduction) AS totalConsumption " +
            "FROM IOTData i " +
            "WHERE EXTRACT(YEAR FROM TO_DATE(i.date, 'YYYY.MM.DD.HH24.MI.SS')) = :year " +
            "GROUP BY DATE(TO_DATE(i.date, 'YYYY.MM.DD.HH24.MI.SS')) "
    )
    List<Integer> findYearlyData(String year);

}
