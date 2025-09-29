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

    @GetMapping("/hasUserLiked")
    public ResponseEntity<Boolean> hasUserLiked(@PathVariable UUID postId, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UUID userId = jwtService.extractUserId(token);
        return ResponseEntity.ok(likeService.hasUserLiked(postId, userId));
    }

    @DeleteMapping
    public ResponseEntity<Void> unlikePost(@PathVariable UUID postId, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UUID userId = jwtService.extractUserId(token);
        likeService.unlikePost(postId, userId);
        return ResponseEntity.noContent().build();
    }


}