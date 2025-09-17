package com.example.fitness_tracker.repository;

import com.example.fitness_tracker.domain.models.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, UUID> {

    /**
     * Get all exercises for a workout whose parent workout is NOT soft-deleted.
     */
    List<WorkoutExercise> findAllByWorkoutIdAndWorkoutDeletedAtIsNull(UUID workoutId);

    /**
     * Find a workout-exercise by its ID ONLY if the parent workout is NOT soft-deleted.
     */
    Optional<WorkoutExercise> findByIdAndWorkoutDeletedAtIsNull(UUID id);
}

