package com.example.fitness_tracker.repository;

import com.example.fitness_tracker.domain.models.User;
import com.example.fitness_tracker.domain.models.Workout;
import com.example.fitness_tracker.domain.models.WorkoutLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkoutLogRepository extends JpaRepository<WorkoutLog, UUID> {
    List<WorkoutLog> findByUserOrderByDateDesc(User user);
    List<WorkoutLog> findByWorkout(Workout workout);
}