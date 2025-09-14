package com.example.fitness_tracker.domain.dto.Goal;

import com.example.fitness_tracker.domain.enums.GoalStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoalDto {
    private UUID id;
    private UUID createdByUserId;
    private String description;
    private Double targetWeight;
    private Double currentWeight;
    private LocalDate deadline;
    private GoalStatus status;
}
