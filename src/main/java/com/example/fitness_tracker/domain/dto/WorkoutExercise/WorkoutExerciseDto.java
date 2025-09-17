package com.example.fitness_tracker.domain.dto.WorkoutExercise;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutExerciseDto {
//    @JsonIgnore
    private UUID id;
    private String exerciseName;
    private String workoutName;
    private Integer sets;
    private Integer reps;
    private Integer duration;
    private Integer calories;
    private Integer orderInWorkout;
    private String notes;
}
