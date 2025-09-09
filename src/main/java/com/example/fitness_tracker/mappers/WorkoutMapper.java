package com.example.fitness_tracker.mappers;

import com.example.fitness_tracker.domain.dto.Workout.WorkoutDto;
import com.example.fitness_tracker.domain.models.Workout;
import com.example.fitness_tracker.domain.models.User;

import java.util.stream.Collectors;

public class WorkoutMapper {

    public static WorkoutDto toDto(Workout workout) {
        if (workout == null) return null;

        return WorkoutDto.builder()
                .id(workout.getId())
                .name(workout.getName())
                .description(workout.getDescription())
                .isShared(workout.isShared())
                .createdById(workout.getCreatedBy().getId())
                .workoutExercises(workout.getWorkoutExercises().stream()
                        .map(WorkoutExerciseMapper::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public static Workout toEntity(WorkoutDto dto, User createdBy) {
        if (dto == null) return null;

        Workout workout = new Workout();
        workout.setId(dto.getId());
        workout.setName(dto.getName());
        workout.setDescription(dto.getDescription());
        workout.setShared(dto.isShared());
        workout.setCreatedBy(createdBy);
        return workout;
    }
}
