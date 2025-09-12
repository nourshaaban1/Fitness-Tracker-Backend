package com.example.fitness_tracker.repository;

import com.example.fitness_tracker.domain.models.Nutrition;
import com.example.fitness_tracker.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface NutritionRepository extends JpaRepository<Nutrition, UUID> {

    Optional<Nutrition> findByIdAndDeletedAtIsNull(UUID id);

    List<Nutrition> findAllByDeletedAtIsNull();
}
