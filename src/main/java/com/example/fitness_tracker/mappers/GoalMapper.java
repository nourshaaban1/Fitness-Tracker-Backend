package com.example.fitness_tracker.mappers;

import com.example.fitness_tracker.domain.dto.Goal.CreateGoalDto;
import com.example.fitness_tracker.domain.dto.Goal.GoalDto;
import com.example.fitness_tracker.domain.models.Goal;
import com.example.fitness_tracker.domain.models.User;
import org.springframework.stereotype.Component;

@Component
public class GoalMapper {
    public GoalDto toGoalDto(Goal goal) {
        return GoalDto.builder()
                .id(goal.getId())
                .createdByUserId(goal.getUser().getId())
                .description(goal.getDescription())
                .targetWeight(goal.getTargetWeight())
                .currentWeight(goal.getCurrentWeight())
                .deadline(goal.getDeadline())
                .status(goal.getStatus())
                .build();
    }

    public Goal toGoal(CreateGoalDto dto, User user) {
        return Goal.builder()
                .description(dto.getDescription())
                .user(user)
                .status(dto.getStatus())
                .targetWeight(dto.getTargetWeight())
                .currentWeight(dto.getCurrentWeight())
                .deadline(dto.getDeadline())
                .build();
    }
}
