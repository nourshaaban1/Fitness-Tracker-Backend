package com.example.fitness_tracker.service;

import com.example.fitness_tracker.domain.models.Like;
import com.example.fitness_tracker.domain.models.Post;
import com.example.fitness_tracker.domain.models.User;
import com.example.fitness_tracker.exceptions.NotFoundException;
import com.example.fitness_tracker.repository.LikeRepository;
import com.example.fitness_tracker.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostRepository postRepository;

    public void likePost(UUID postId, UUID userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found"));
        Optional<Like> existingLike = likeRepository.findByPostIdAndUserId(postId, userId);
        if (existingLike.isPresent()) {
            return; // Already liked
        }
        Like like = new Like();
        like.setPost(post);
        User user = new User();
        user.setId(userId);
        like.setUser(user);
        likeRepository.save(like);
    }

    public void unlikePost(UUID postId, UUID userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found"));
        Optional<Like> existingLike = likeRepository.findByPostIdAndUserId(postId, userId);
        existingLike.ifPresent(like -> {
            like.softDelete();
            likeRepository.save(like);
        });
    }
}