package com.example.fitness_tracker.mappers;

import com.example.fitness_tracker.domain.dto.WorkoutExercise.WorkoutExerciseDto;
import com.example.fitness_tracker.domain.models.Exercise;
import com.example.fitness_tracker.domain.models.Workout;
import com.example.fitness_tracker.domain.models.WorkoutExercise;

public class WorkoutExerciseMapper {

    public static WorkoutExerciseDto toDto(WorkoutExercise we) {
        if (we == null) return null;

        return WorkoutExerciseDto.builder()
                .id(we.getId())
                .workoutId(we.getWorkout().getId())
                .exerciseId(we.getExercise().getId())
                .sets(we.getSets())
                .reps(we.getReps())
                .duration(we.getDuration())
                .calories(we.getCalories())
                .orderInWorkout(we.getOrderInWorkout())
                .notes(we.getNotes())
                .build();
    }

    public static WorkoutExercise toEntity(WorkoutExerciseDto dto, Workout workout, Exercise exercise) {
        if (dto == null) return null;

        WorkoutExercise we = new WorkoutExercise();
        we.setId(dto.getId());
        we.setWorkout(workout);
        we.setExercise(exercise);
        we.setSets(dto.getSets());
        we.setReps(dto.getReps());
        we.setDuration(dto.getDuration());
        we.setCalories(dto.getCalories());
        we.setOrderInWorkout(dto.getOrderInWorkout());
        we.setNotes(dto.getNotes());
        return we;
    }
}
