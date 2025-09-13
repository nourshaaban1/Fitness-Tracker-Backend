package com.example.fitness_tracker.repository;

import com.example.fitness_tracker.domain.models.Goal;
import com.example.fitness_tracker.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GoalRepository extends JpaRepository<Goal, UUID> {
    List<Goal> findAllByUserAndDeletedAtIsNull(User user);
    Optional<Goal> findByIdAndDeletedAtIsNull(UUID id);
    Optional<Goal> findByIdAndUserAndDeletedAtIsNull(UUID id, User user);
}