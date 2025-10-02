package com.example.fitness_tracker.domain.dto.Goal;

import com.example.fitness_tracker.domain.enums.GoalStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CreateGoalDto {
    @NotBlank(message = "description can't be empty") @Size(max = 50, message = "description can't be more than 50")
    private String description;
    @NotNull(message = "target weight required")
    @Positive(message = "targetWeight must be > 0")
    private Double targetWeight;
    @NotNull(message = "current weight required")
    @Positive(message = "currentWeight must be > 0")
    private Double currentWeight;
    @NotNull(message = "deadline required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Future(message = "deadline must be in the future")
    private LocalDate deadline;
    @Builder.Default
    private GoalStatus status = GoalStatus.NOT_STARTED;
}
