package com.example.fitness_tracker.domain.dto.Goal;

import com.example.fitness_tracker.domain.enums.GoalStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateGoalDto {
    private String description;
    private Double CurrentWeight;
    private Double targetWeight;
    private String deadline;
    private GoalStatus status;
}
