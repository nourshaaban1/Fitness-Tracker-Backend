package com.example.fitness_tracker.domain.dto.WorkoutExercise;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutExerciseDto {
    private UUID id;
    private UUID exerciseId;
    private Integer sets;
    private Integer reps;
    private Integer duration;
    private Integer calories;
    private Integer orderInWorkout;
    private String notes;
}
