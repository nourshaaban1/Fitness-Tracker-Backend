package com.example.fitness_tracker.service;

import com.example.fitness_tracker.domain.dto.Goal.CreateGoalDto;
import com.example.fitness_tracker.domain.dto.Goal.GoalDto;
import com.example.fitness_tracker.domain.dto.Goal.UpdateGoalDto;
import com.example.fitness_tracker.domain.models.Goal;
import com.example.fitness_tracker.domain.models.User;
import com.example.fitness_tracker.mappers.GoalMapper;
import com.example.fitness_tracker.repository.GoalRepository;
import com.example.fitness_tracker.repository.UserRepository;
import com.example.fitness_tracker.util.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class GoalService {
    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final GoalMapper goalMapper;

    public List<GoalDto> getAllGoalsForCurrentUser() {
        UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new RuntimeException("User not logged in"));
        return goalRepository.findAllByUserAndDeletedAtIsNull(user).stream()
                .map(goalMapper::toGoalDto)
                .toList();
    }

    public GoalDto getGoalByIdForCurrentUser(UUID id) {
        UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new RuntimeException("User not logged in"));
        Goal goal = goalRepository.findByIdAndUserAndDeletedAtIsNull(id, user).orElseThrow(() -> new EntityNotFoundException("goal", id));
        return goalMapper.toGoalDto(goal);
    }

    public GoalDto createGoal(CreateGoalDto dto) {
        UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new RuntimeException("User not logged in"));
        Goal goal = goalMapper.toGoal(dto, user);
        return goalMapper.toGoalDto(goalRepository.save(goal));
    }

    public GoalDto updateGoal(UUID id, UpdateGoalDto dto) {
        Goal goal = goalRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new EntityNotFoundException("goal", id));
        if(dto.getCurrentWeight() != null) goal.setCurrentWeight(dto.getCurrentWeight());
        if(dto.getStatus() != null) goal.setStatus(dto.getStatus());
        if (dto.getDescription() != null) goal.setDescription(goal.getDescription());
        return  goalMapper.toGoalDto(goalRepository.save(goal));
    }

    public void delete(UUID id) {
        Goal existing = goalRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Goal", id));
        existing.softDelete();
        goalRepository.save(existing);
    }

    public void restore(UUID id) {
        Goal existing = goalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Goal", id));
        existing.restore();
        goalRepository.save(existing);
    }
}
