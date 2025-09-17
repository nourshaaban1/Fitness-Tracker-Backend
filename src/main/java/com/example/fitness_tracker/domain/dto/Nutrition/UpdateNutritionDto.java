package com.example.fitness_tracker.domain.dto.Nutrition;

import com.example.fitness_tracker.domain.enums.NutritionCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateNutritionDto {
    @NotBlank private String name;
    @NotBlank @Positive private Float caloriesPer100g;
    @NotBlank @Positive private Float proteinPer100g;
    @NotBlank @Positive private Float carbsPer100g;
    @NotBlank @Positive private Float fatsPer100g;
    private Float fiberPer100g;
    private Float sugarPer100g;
    @NotBlank private NutritionCategory category;
}
