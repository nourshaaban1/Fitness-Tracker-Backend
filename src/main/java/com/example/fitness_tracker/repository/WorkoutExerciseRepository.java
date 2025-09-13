package com.example.fitness_tracker.repository;

import com.example.fitness_tracker.domain.models.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, UUID> {}
