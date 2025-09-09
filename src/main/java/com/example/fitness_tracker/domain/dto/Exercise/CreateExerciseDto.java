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
public class CreateExerciseDto {
    @NotBlank(message = "Exercise name is required")
    @Size(max = 150, message = "Exercise name cannot exceed 150 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Category is required")
    private ExerciseCategory category;

    private String imageUrl;

    @NotNull(message = "Tracking mode is required")
    private TrackingMode trackingMode;

    private boolean hasWeights;
}
