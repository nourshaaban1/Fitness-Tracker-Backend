package com.example.fitness_tracker.mappers;

import com.example.fitness_tracker.domain.dto.Exercise.ExerciseDto;
import com.example.fitness_tracker.domain.dto.Exercise.UpdateExerciseDto;
import com.example.fitness_tracker.domain.models.Exercise;

public class ExerciseMapper {

    public static ExerciseDto toDto(Exercise exercise) {
        if (exercise == null) return null;

        return ExerciseDto.builder()
                .id(exercise.getId())
                .name(exercise.getName())
                .category(exercise.getCategory())
                .imageUrl(exercise.getImageUrl())
                .description(exercise.getDescription())
                .trackingMode(exercise.getTrackingMode())
                .hasWeights(exercise.isHasWeights())
                .createdByUserId(exercise.getCreatedBy().getId())
                .build();
    }

    public static void updateEntityFromDto(Exercise e, UpdateExerciseDto dto) {
        if (dto.getName() != null) e.setName(dto.getName());
        if (dto.getCategory() != null) e.setCategory(dto.getCategory());
        if (dto.getImageUrl() != null) e.setImageUrl(dto.getImageUrl());
        if (dto.getDescription() != null) e.setDescription(dto.getDescription());
        if (dto.getTrackingMode() != null) e.setTrackingMode(dto.getTrackingMode());
        if (dto.isHasWeights()) e.setHasWeights(true);
    }
}
