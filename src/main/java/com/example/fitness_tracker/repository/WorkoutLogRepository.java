package com.example.fitness_tracker.repository;

import com.example.fitness_tracker.domain.models.Workout;
import com.example.fitness_tracker.domain.models.WorkoutLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkoutLogRepository extends JpaRepository<WorkoutLog, UUID> {
    List<WorkoutLog> findByWorkout(Workout workout);

    Optional<WorkoutLog> findByIdAndUserIdAndDeletedAtIsNull(UUID logId, UUID userId);
    List<WorkoutLog> findAllByUserIdAndDeletedAtIsNull(UUID userId);
    List<WorkoutLog> findAllByDeletedAtIsNull();
}