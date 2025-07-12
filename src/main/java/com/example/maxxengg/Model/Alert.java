package com.example.maxxengg.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Data
@Table(name = "alerts")
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imei;

    private String description;

    private String status;

    @Column(name = "is_resolved")
    private Boolean isResolved;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Automatically set timestamp before persisting
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
