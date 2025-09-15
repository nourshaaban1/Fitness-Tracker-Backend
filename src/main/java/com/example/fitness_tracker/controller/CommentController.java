package com.example.fitness_tracker.controller;

import com.example.fitness_tracker.domain.dto.Community.PostCommentsResponse;
import com.example.fitness_tracker.domain.models.User;
import com.example.fitness_tracker.domain.dto.Community.CommentCreateDto;
import com.example.fitness_tracker.domain.dto.Community.CommentResponseDto;
import com.example.fitness_tracker.security.JwtService;
import com.example.fitness_tracker.service.AuthService;
import com.example.fitness_tracker.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private AuthService authService;

    @GetMapping
    public ResponseEntity<PostCommentsResponse> getCommentsByPostId(@PathVariable UUID postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable UUID postId, @RequestBody CommentCreateDto dto, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UUID userId = jwtService.extractUserId(token);
        CommentResponseDto response = commentService.createComment(postId, dto, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable UUID id, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UUID userId = jwtService.extractUserId(token);

        commentService.deleteComment(id, userId);
        return ResponseEntity.noContent().build();
    }
}