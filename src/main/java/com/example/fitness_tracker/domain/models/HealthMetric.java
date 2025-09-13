package com.example.fitness_tracker.domain.models;

import com.example.fitness_tracker.domain.models.auditable.BaseEntityWithSoftDelete;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "health_metrics",
    indexes = {
        @Index(name = "idx_health_metrics_user_id", columnList = "user_id")
    }
)
public class HealthMetric extends BaseEntityWithSoftDelete {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private float weight; // in kg

    @Column(nullable = false)
    private float height; // in cm

    @Column(nullable = false)
    private float bmi;

    @Column(name = "body_measurements", columnDefinition = "json")
    private String bodyMeasurements; // JSON string (e.g., {"waist": 80, "chest": 100})

    @Column(name = "metric_date", nullable = false)
    private LocalDate date;

    /** Auto-calculate BMI before persisting */
    @PrePersist
    @PreUpdate
    private void calculateBmi() {
        if (height > 0 && weight > 0) {
            float heightInMeters = height / 100f;
            this.bmi = weight / (heightInMeters * heightInMeters);
        }
    }
}