package com.example.fitness_tracker.domain.dto.Nutrition;

import com.example.fitness_tracker.domain.enums.NutritionCategory;
import jakarta.validation.constraints.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNutritionDto {
    @NotBlank private String name;
    @NotNull @Positive(message = "caloriesPer100g must be > 0") private Float caloriesPer100g;
    @NotNull @Positive(message = "proteinPer100g must be > 0") private Float proteinPer100g;
    @NotNull @Positive(message = "carbsPer100g must be > 0") private Float carbsPer100g;
    @NotNull @Positive(message = "fatsPer100g must be > 0") private Float fatsPer100g;
    @PositiveOrZero(message = "fiberPer100g must be >= 0") private Float fiberPer100g;
    @PositiveOrZero(message = "sugarPer100g must be >= 0") private Float sugarPer100g;
    @NotNull private NutritionCategory category;
}
