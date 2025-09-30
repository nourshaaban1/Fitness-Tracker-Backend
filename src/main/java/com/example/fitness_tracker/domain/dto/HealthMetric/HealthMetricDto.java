package com.example.fitness_tracker.domain.dto.HealthMetric;

import jakarta.validation.Valid; 
import jakarta.validation.constraints.*;
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

    @Positive(message = "Weight must be greater than 0") 
    @DecimalMin(value = "20.0", message = "Weight is unrealistically low") 
    @DecimalMax(value = "500.0", message = "Weight is unrealistically high") 
    private float weight;
    
    @Positive(message = "Height must be greater than 0") 
    @DecimalMin(value = "50.0", message = "Height is unrealistically low") 
    @DecimalMax(value = "300.0", message = "Height is unrealistically high") 
    private float height; 

    private float bmi; 
    
    @Valid 
    private BodyMeasurementsDto bodyMeasurements; // JSON string

    @NotNull(message = "Date cannot be null") 
    @PastOrPresent(message = "Date cannot be in the future") 
    private LocalDate date; 
}