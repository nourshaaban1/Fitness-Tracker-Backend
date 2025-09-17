package com.example.fitness_tracker.repository;

import com.example.fitness_tracker.domain.models.Exercise;
import com.example.fitness_tracker.domain.models.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface WorkoutRepository extends JpaRepository<Workout, UUID>, JpaSpecificationExecutor<Workout> {
    Optional<Workout> findByIdAndDeletedAtIsNull(UUID id);

    List<Workout> findAllByDeletedAtIsNull();
}
