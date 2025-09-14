package com.example.fitness_tracker.domain.dto.HealthMetric;

import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthMetricDto {
    private UUID id;
    private float weight;
    private float height;
    private float bmi;
    private BodyMeasurementsDto bodyMeasurements; // JSON string
    private LocalDate date;
}