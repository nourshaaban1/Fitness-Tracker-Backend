package com.example.fitness_tracker.repository;

import com.example.fitness_tracker.domain.models.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, UUID>, JpaSpecificationExecutor<Exercise> {
    Optional<Exercise> findByIdAndDeletedAtIsNull(UUID id);
    Optional<Exercise> findByNameAndDeletedAtIsNull(String name);
    List<Exercise> findAllByDeletedAtIsNull();
}
