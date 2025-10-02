package com.example.fitness_tracker.domain.dto.Goal;

import com.example.fitness_tracker.domain.enums.GoalStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateGoalDto {
    private String description;
    @Positive(message = "currentWeight must be > 0")
    private Double currentWeight;
    @Positive(message = "targetWeight must be > 0")
    private Double targetWeight;
    private String deadline;
    private GoalStatus status;
}
