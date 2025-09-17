package com.example.fitness_tracker.domain.dto.Community;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class PostResponseDto {
    private UUID postId;
    private UUID userId;
    private String content;
    private String imageUrl;
    private Instant createdAt;
    private int likeCount;
    private int commentCount;
}