package com.example.fitness_tracker.controller;

import com.example.fitness_tracker.domain.dto.Community.PostLikesResponse;
import com.example.fitness_tracker.domain.models.Like;
import com.example.fitness_tracker.security.JwtService;
import com.example.fitness_tracker.service.AuthService;
import com.example.fitness_tracker.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/likes")
public class LikeController {

    private final JwtService jwtService;

    private final LikeService likeService;

    private final AuthService authService;




    @PostMapping
    public ResponseEntity<Void> likePost(@PathVariable UUID postId, @RequestHeader("Authorization") String authHeader)
    {
        String token = authHeader.replace("Bearer ", "");
        UUID userId = jwtService.extractUserId(token);
        likeService.likePost(postId, userId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> unlikePost(@PathVariable UUID postId, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UUID userId = jwtService.extractUserId(token);
        likeService.unlikePost(postId, userId);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Get the number of likes and list of user IDs who liked a specific post
     * @param postId The ID of the post
     * @return PostLikesResponse containing total likes and list of user IDs
     */
    @GetMapping()
    public ResponseEntity<PostLikesResponse> getPostLikes(@PathVariable UUID postId) {
        return ResponseEntity.ok(likeService.getPostLikesResponse(postId));
    }
}