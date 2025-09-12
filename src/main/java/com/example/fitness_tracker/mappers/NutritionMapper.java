package com.example.fitness_tracker.mappers;

import com.example.fitness_tracker.domain.dto.Nutrition.UpdateNutritionDto;
import com.example.fitness_tracker.domain.models.Nutrition;
import com.example.fitness_tracker.domain.dto.Nutrition.NutritionDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NutritionMapper {

    public NutritionDto toDto(Nutrition n) {
        if (n == null) return null;
        return NutritionDto.builder()
                .id(n.getId())
                .name(n.getName())
                .caloriesPer100g(n.getCaloriesPer100g())
                .proteinPer100g(n.getProteinPer100g())
                .carbsPer100g(n.getCarbsPer100g())
                .fatsPer100g(n.getFatsPer100g())
                .fiberPer100g(n.getFiberPer100g())
                .sugarPer100g(n.getSugarPer100g())
                .category(n.getCategory())
                .build();
    }

    public void updateEntityFromDto(Nutrition n, UpdateNutritionDto dto) {
        if (dto.getName() != null) n.setName(dto.getName());
        if (dto.getCaloriesPer100g() != null) n.setCaloriesPer100g(dto.getCaloriesPer100g());
        if (dto.getProteinPer100g() != null) n.setProteinPer100g(dto.getProteinPer100g());
        if (dto.getCarbsPer100g() != null) n.setCarbsPer100g(dto.getCarbsPer100g());
        if (dto.getFatsPer100g() != null) n.setFatsPer100g(dto.getFatsPer100g());
        if (dto.getFiberPer100g() != null) n.setFiberPer100g(dto.getFiberPer100g());
        if (dto.getSugarPer100g() != null) n.setSugarPer100g(dto.getSugarPer100g());
        if (dto.getCategory() != null) n.setCategory(dto.getCategory());
    }


    public List<NutritionDto> toDtoList(List<Nutrition> list) {
        return list.stream().map(this::toDto).toList();
    }
}
