package com.example.maxxengg.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "iotdata",schema = "public")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IOTData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "imie", length = 255, nullable = false)
    private String imie;

    @Column(name = "date", length = 255, nullable = false)
    private String date;

    @Column(name = "pv1_voltage")
    private Long pv1Voltage;

    @Column(name = "pv1_current")
    private Long pv1Current;

    @Column(name = "pv2_voltage")
    private Long pv2Voltage;

    @Column(name = "pv2_current")
    private Long pv2Current;

    @Column(name = "pv1_power")
    private Long pv1Power;

    @Column(name = "pv2_power")
    private Long pv2Power;

    @Column(name = "output_active_power")
    private Long outputActivePower;

    @Column(name = "output_reactive_power")
    private Long outputReactivePower;

    @Column(name = "grid_frequency")
    private Long gridFrequency;

    @Column(name = "a_phase_voltage")
    private Long aPhaseVoltage;

    @Column(name = "a_phase_current")
    private Long aPhaseCurrent;

    @Column(name = "b_phase_voltage")
    private Long bPhaseVoltage;

    @Column(name = "b_phase_current")
    private Long bPhaseCurrent;

    @Column(name = "c_phase_voltage")
    private Long cPhaseVoltage;

    @Column(name = "c_phase_current")
    private Long cPhaseCurrent;

    @Column(name = "total_production_high_byte")
    private Long totalProductionHighByte;

    @Column(name = "total_production_low_bytes")
    private Long totalProductionLowBytes;

    @Column(name = "today_production")
    private Long todayProduction;

    @Column(name = "today_generation_time")
    private Long todayGenerationTime;

    @Column(name = "inverter_module_temperature")
    private Long inverterModuleTemperature;

    @Column(name = "inverter_inner_temperature")
    private Long inverterInnerTemperature;

    @Column(name = "inverter_bus_voltage")
    private Long inverterBusVoltage;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


}
