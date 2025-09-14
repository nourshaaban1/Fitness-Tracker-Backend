package com.example.fitness_tracker.domain.dto.Community;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class CommentResponseDto {
    private UUID commentId;
    private UUID userId;
    private String content;
    private Instant createdAt;
}