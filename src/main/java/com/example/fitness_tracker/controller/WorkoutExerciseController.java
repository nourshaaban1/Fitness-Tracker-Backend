package com.example.fitness_tracker.controller;

import com.example.fitness_tracker.domain.dto.WorkoutExercise.CreateWorkoutExerciseDto;
import com.example.fitness_tracker.domain.dto.WorkoutExercise.WorkoutExerciseDto;
import com.example.fitness_tracker.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutExerciseController {

    private final WorkoutService workoutService;

    @GetMapping("/{workoutId}/exercises")
    public List<WorkoutExerciseDto> getWorkoutExercises(@PathVariable UUID workoutId) {
        return workoutService.getWorkoutExercises(workoutId);
    }

    @GetMapping("/{workoutId}/exercises/{exerciseId}")
    public WorkoutExerciseDto getWorkoutExercise(
            @PathVariable UUID workoutId,
            @PathVariable UUID exerciseId
    ) {
        // workoutId is not strictly needed for the lookup,
        // but it's nice for validation or nested URL structure.
        return workoutService.getWorkoutExercise(exerciseId);
    }

    @PutMapping("/{workoutId}/exercises/{exerciseId}")
    public WorkoutExerciseDto updateWorkoutExercise(
            @PathVariable UUID workoutId,
            @PathVariable UUID exerciseId,
            @RequestBody CreateWorkoutExerciseDto dto
    ) {
        return workoutService.updateWorkoutExercise(exerciseId, dto);
    }

    @DeleteMapping("/{workoutId}/exercises/{exerciseId}")
    public void deleteWorkoutExercise(
            @PathVariable UUID workoutId,
            @PathVariable UUID exerciseId
    ) {
        workoutService.deleteWorkoutExercise(exerciseId);
    }
}
