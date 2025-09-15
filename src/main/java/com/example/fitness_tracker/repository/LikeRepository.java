package com.example.fitness_tracker.repository;

import com.example.fitness_tracker.domain.models.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {

    // Using derived query methods
    Optional<Like> findByPostIdAndUserId(UUID postId, UUID userId);

    // Optional: find by post id
    Optional<Like> findByPostId(UUID postId);

    // Optional: delete by post and user
    void deleteByPostIdAndUserId(UUID postId, UUID userId);

    // Optional: check if user already liked a post
    boolean existsByPostIdAndUserId(UUID postId, UUID userId);
    
    // Find all likes for a specific post
    List<Like> findAllByPostId(UUID postId);
}
