package com.example.fitness_tracker.service;

import com.example.fitness_tracker.domain.dto.Nutrition.CreateNutritionDto;
import com.example.fitness_tracker.domain.dto.Nutrition.NutritionDto;
import com.example.fitness_tracker.domain.dto.Nutrition.UpdateNutritionDto;
import com.example.fitness_tracker.domain.models.Nutrition;
import com.example.fitness_tracker.mappers.NutritionMapper;
import com.example.fitness_tracker.repository.NutritionRepository;
import com.example.fitness_tracker.util.EntityNotFoundException;
import com.example.fitness_tracker.util.InvalidEntityDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class NutritionService {

    private final NutritionRepository nutritionRepository;
    private final NutritionMapper mapper;

    public List<NutritionDto> getAll() {
        return mapper.toDtoList(nutritionRepository.findAllByDeletedAtIsNull());
    }

    public NutritionDto getById(UUID id) {
        Nutrition n = nutritionRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Nutrition", id));
        return mapper.toDto(n);
    }

    public NutritionDto create(CreateNutritionDto dto) {
        if (dto == null) {
            throw new InvalidEntityDataException("Please provide valid nutrition data");
        }

        Nutrition n = Nutrition.builder()
                .name(dto.getName())
                .caloriesPer100g(dto.getCaloriesPer100g())
                .proteinPer100g(dto.getProteinPer100g())
                .carbsPer100g(dto.getCarbsPer100g())
                .fatsPer100g(dto.getFatsPer100g())
                .fiberPer100g(dto.getFiberPer100g())
                .sugarPer100g(dto.getSugarPer100g())
                .category(dto.getCategory())
                .build();

        return mapper.toDto(nutritionRepository.save(n));
    }

    public NutritionDto update(UUID id, UpdateNutritionDto dto) {
        Nutrition existing = nutritionRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Nutrition", id));

        if (dto == null) {
            throw new InvalidEntityDataException("Update data cannot be null");
        }

        mapper.updateEntityFromDto(existing, dto);
        return mapper.toDto(nutritionRepository.save(existing));
    }

    public void delete(UUID id) {
        Nutrition existing = nutritionRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Nutrition", id));
        existing.softDelete();
        nutritionRepository.save(existing);
    }

    public void restore(UUID id) {
        Nutrition existing = nutritionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nutrition", id));
        existing.restore();
        nutritionRepository.save(existing);
    }
}
