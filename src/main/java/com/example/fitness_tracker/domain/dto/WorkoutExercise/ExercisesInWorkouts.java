package com.example.fitness_tracker.domain.dto.WorkoutExercise;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExercisesInWorkouts {
    private String exerciseName;
    private Integer sets;
    private Integer reps;
    private Integer duration;
    private Integer calories;
    private Integer orderInWorkout;
    private String notes;
}
