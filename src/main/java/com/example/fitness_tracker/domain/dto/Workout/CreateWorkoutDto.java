package com.example.fitness_tracker.domain.dto.Workout;

import com.example.fitness_tracker.domain.dto.WorkoutExercise.CreateWorkoutExerciseDto;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateWorkoutDto {
    private String name;
    private String description;
    private boolean isShared;
//    private UUID createdById; // ID of the user creating the workout
    private List<CreateWorkoutExerciseDto> workoutExercises; // Exercises to add
}
