package com.example.fitness_tracker.repository;

import com.example.fitness_tracker.domain.models.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {

    // Using derived query methods
    Optional<Like> findByPostIdAndUserId(UUID postId, UUID userId);

    // Optional: delete by post and user
    void deleteByPostIdAndUserId(UUID postId, UUID userId);

    // Optional: check if user already liked a post
    boolean existsByPostIdAndUserId(UUID postId, UUID userId);
}
