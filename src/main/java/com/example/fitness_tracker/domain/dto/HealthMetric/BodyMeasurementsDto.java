package com.example.fitness_tracker.domain.dto.HealthMetric;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.PositiveOrZero; 
import lombok.*; 

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class BodyMeasurementsDto { 
    @PositiveOrZero(message = "Chest measurement cannot be negative") 
    @DecimalMax(value = "300.0", message = "Chest measurement is unrealistically large") 
    private float chest; 

    @PositiveOrZero(message = "Waist measurement cannot be negative") 
    @DecimalMax(value = "300.0", message = "Waist measurement is unrealistically large") 
    private float waist; 

    @PositiveOrZero(message = "Hips measurement cannot be negative") 
    @DecimalMax(value = "300.0", message = "Hips measurement is unrealistically large") 
    private float hips; 

    @PositiveOrZero(message = "Neck measurement cannot be negative") 
    @DecimalMax(value = "100.0", message = "Neck measurement is unrealistically large") 
    private float neck; 
    
    @PositiveOrZero(message = "Biceps measurement cannot be negative") 
    @DecimalMax(value = "100.0", message = "Biceps measurement is unrealistically large") 
    private float biceps;
    
    @PositiveOrZero(message = "Thighs measurement cannot be negative") 
    @DecimalMax(value = "200.0", message = "Thighs measurement is unrealistically large") 
    private float thighs;
    
    @PositiveOrZero(message = "Calves measurement cannot be negative") 
    @DecimalMax(value = "150.0", message = "Calves measurement is unrealistically large") 
    private float calves; 
}