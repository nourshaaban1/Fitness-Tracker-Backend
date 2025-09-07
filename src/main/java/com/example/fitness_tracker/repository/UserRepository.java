package com.example.fitness_tracker.repository;

import com.example.fitness_tracker.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmailAndDeletedAtIsNull(String email);

    // Check if a non-deleted email exists
    boolean existsByEmailAndDeletedAtIsNull(String email);
}