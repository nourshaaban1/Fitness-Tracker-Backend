package com.example.fitness_tracker.domain.dto.Exercise;

import com.example.fitness_tracker.domain.enums.ExerciseCategory;
import com.example.fitness_tracker.domain.enums.TrackingMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateExerciseDto {
    @Size(max = 150, message = "Exercise name cannot exceed 150 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private ExerciseCategory category;

    private String imageUrl;

    private TrackingMode trackingMode;

    private boolean hasWeights;
}
