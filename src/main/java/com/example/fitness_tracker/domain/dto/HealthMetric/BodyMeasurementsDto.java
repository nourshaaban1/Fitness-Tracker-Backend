package com.example.fitness_tracker.domain.dto.HealthMetric;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BodyMeasurementsDto {
    private float chest;
    private float waist;
    private float hips;
    private float neck;
    private float biceps;
    private float thighs;
    private float calves;
}