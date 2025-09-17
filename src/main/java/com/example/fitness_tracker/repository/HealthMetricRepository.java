package com.example.fitness_tracker.repository;

import com.example.fitness_tracker.domain.models.HealthMetric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HealthMetricRepository extends JpaRepository<HealthMetric, UUID> {
    List<HealthMetric> findAllByUserIdAndDeletedAtIsNull(UUID userId);
    Optional<HealthMetric> findByIdAndUserIdAndDeletedAtIsNull(UUID id, UUID userId);
    List<HealthMetric> findAllByDeletedAtIsNull();
}