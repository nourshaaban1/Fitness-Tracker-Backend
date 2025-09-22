package com.example.fitness_tracker.domain.dto.Community;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostCommentsResponse {
    private long totalComments;
    private List<CommentResponseDto> comments;
}
