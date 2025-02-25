package com.example.maxxengg.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "plantDetails")
public class Plantdetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plant_id;

    @Column(nullable = false)
    private String plant_name;

    @Column(nullable = false)
    private Double lat; // ✅ Remove precision & scale, keep Double

    @Column(nullable = false)
    private Double log; // ✅ Remove precision & scale, keep Double

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at = LocalDateTime.now();
}
