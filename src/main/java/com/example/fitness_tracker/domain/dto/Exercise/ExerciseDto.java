package com.example.fitness_tracker.domain.dto.Exercise;

import com.example.fitness_tracker.domain.enums.ExerciseCategory;
import com.example.fitness_tracker.domain.enums.TrackingMode;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseDto {
    private UUID id;
    private String name;
    private ExerciseCategory category;
    private String imageUrl;
    private String description;
    private TrackingMode trackingMode;
    private boolean hasWeights;
    private UUID createdByUserId;
//    private List<WorkoutExerciseDto> workoutExercises;
}
