package com.example.fitness_tracker.domain.dto.Goal;

import com.example.fitness_tracker.domain.enums.GoalStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CreateGoalDto {
    @NotBlank(message = "description can't be empty") @Size(max = 50, message = "description can't be more than 50")
    private String description;
    @NotNull(message = "target weight required")
    @Positive
    private Double targetWeight;
    @NotNull(message = "current weight required")
    @Positive
    private Double currentWeight;
    @NotNull(message = "deadline required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;
    @Builder.Default
    private GoalStatus status = GoalStatus.NOT_STARTED;
}
