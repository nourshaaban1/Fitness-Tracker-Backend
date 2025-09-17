package com.example.fitness_tracker.controller;

import com.example.fitness_tracker.security.JwtService;
import com.example.fitness_tracker.service.AuthService;
import com.example.fitness_tracker.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/likes")
public class LikeController {

    private final JwtService jwtService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<Void> likePost(@PathVariable UUID postId, @RequestHeader("Authorization") String authHeader)
    {
        String token = authHeader.replace("Bearer ", "");
        UUID userId = jwtService.extractUserId(token);
        likeService.likePost(postId, userId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> unlikePost(@PathVariable UUID postId, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UUID userId = jwtService.extractUserId(token);
        likeService.unlikePost(postId, userId);
        return ResponseEntity.noContent().build();
    }
}