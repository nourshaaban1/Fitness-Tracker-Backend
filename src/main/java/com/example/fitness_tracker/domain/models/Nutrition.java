package com.example.fitness_tracker.domain.models;

import com.example.fitness_tracker.domain.models.auditable.BaseEntityWithSoftDelete;
import jakarta.persistence.*;
import lombok.*;
import com.example.fitness_tracker.domain.enums.NutritionCategory;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "nutrition")
public class Nutrition extends BaseEntityWithSoftDelete {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "calories_per_100g", nullable = false)
    private Float caloriesPer100g;

    @Column(name = "protein_per_100g")
    private Float proteinPer100g;

    @Column(name = "carbs_per_100g")
    private Float carbsPer100g;

    @Column(name = "fats_per_100g")
    private Float fatsPer100g;

    @Column(name = "fiber_per_100g")
    private Float fiberPer100g;

    @Column(name = "sugar_per_100g")
    private Float sugarPer100g;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private NutritionCategory category;
}
