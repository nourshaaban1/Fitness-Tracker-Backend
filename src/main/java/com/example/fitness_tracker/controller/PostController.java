// File: src/main/java/com/example/fitness_tracker/controller/PostController.java
package com.example.fitness_tracker.controller;

import com.example.fitness_tracker.domain.models.User;
import com.example.fitness_tracker.domain.dto.Community.PostCreateDto;
import com.example.fitness_tracker.domain.dto.Community.PostResponseDto;
import com.example.fitness_tracker.security.JwtService;
import com.example.fitness_tracker.service.PostService;
import com.example.fitness_tracker.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final JwtService jwtService;

    private final PostService postService;

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostCreateDto dto, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UUID userId = jwtService.extractUserId(token);
        User currentUser = new User();
        currentUser.setId(userId);
        PostResponseDto response = postService.createPost(dto, currentUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> getAllPosts(Pageable pageable) {
        return ResponseEntity.ok(postService.getAllPosts(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable UUID id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UUID userId = jwtService.extractUserId(token);
        User currentUser = new User();
        currentUser.setId(userId);
        postService.deletePost(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}