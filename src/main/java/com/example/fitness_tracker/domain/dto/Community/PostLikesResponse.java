package com.example.fitness_tracker.domain.dto.Community;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostLikesResponse {
    private long totalLikes;
    private List<UUID> likedByUserIds;
}
