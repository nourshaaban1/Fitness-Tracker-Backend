package com.example.fitness_tracker.domain.dto.Workout;

import com.example.fitness_tracker.domain.dto.WorkoutExercise.WorkoutExerciseDto;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutDto {
    private UUID id;
    private String name;
    private String description;
    private boolean isShared;
    private UUID createdById;
    private List<WorkoutExerciseDto> workoutExercises;
}
