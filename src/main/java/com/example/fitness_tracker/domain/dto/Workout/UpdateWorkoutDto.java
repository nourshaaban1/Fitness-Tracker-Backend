package com.example.fitness_tracker.domain.dto.Workout;
import com.example.fitness_tracker.domain.dto.WorkoutExercise.CreateWorkoutExerciseDto;
import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateWorkoutDto {
    private String name;
    private String description;
    private Boolean isShared;
    private List<CreateWorkoutExerciseDto> workoutExercises;
}
