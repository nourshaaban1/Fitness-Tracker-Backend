package com.example.fitness_tracker.domain.dto.Nutrition;

import com.example.fitness_tracker.domain.enums.NutritionCategory;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutritionDto {
    private UUID id;
    private String name;
    private Float caloriesPer100g;
    private Float proteinPer100g;
    private Float carbsPer100g;
    private Float fatsPer100g;
    private Float fiberPer100g;
    private Float sugarPer100g;
    private NutritionCategory category;
}
