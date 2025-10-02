package com.example.fitness_tracker.domain.dto.Nutrition;

import com.example.fitness_tracker.domain.enums.NutritionCategory;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateNutritionDto {
    private String name;
    @Positive(message = "caloriesPer100g must be > 0") private Float caloriesPer100g;
    @Positive(message = "proteinPer100g must be > 0") private Float proteinPer100g;
    @Positive(message = "carbsPer100g must be > 0") private Float carbsPer100g;
    @Positive(message = "fatsPer100g must be > 0") private Float fatsPer100g;
    @PositiveOrZero(message = "fiberPer100g must be >= 0") private Float fiberPer100g;
    @PositiveOrZero(message = "sugarPer100g must be >= 0") private Float sugarPer100g;
    private NutritionCategory category;
}
