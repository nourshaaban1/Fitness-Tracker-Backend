package com.example.fitness_tracker.service;

import com.example.fitness_tracker.domain.models.Comment;
import com.example.fitness_tracker.domain.models.Post;
import com.example.fitness_tracker.domain.models.User;
import com.example.fitness_tracker.domain.dto.Community.CommentCreateDto;
import com.example.fitness_tracker.domain.dto.Community.CommentResponseDto;
import com.example.fitness_tracker.exceptions.NotFoundException;
import com.example.fitness_tracker.exceptions.UnauthorizedException;
import com.example.fitness_tracker.repository.CommentRepository;
import com.example.fitness_tracker.repository.PostRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Getter

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    public CommentResponseDto createComment(UUID postId, CommentCreateDto dto, UUID userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found"));
        Comment comment = new Comment();
        comment.setPost(post);
        User user = new User();
        user.setId(userId);
        comment.setUser(user);
        comment.setContent(dto.getContent());
        comment = commentRepository.save(comment);
        return mapToResponseDto(comment);
    }

    public void deleteComment(UUID id, UUID userId) {
        User currentUser = new User();
        currentUser.setId(userId);
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Comment not found"));
        if (!comment.getUser().getId().equals(userId) && !"ADMIN".equals(currentUser.getRole())) {
            throw new UnauthorizedException("Not authorized to delete this comment");
        }
        comment.softDelete();
        commentRepository.save(comment);
    }

    private CommentResponseDto mapToResponseDto(Comment comment) {
        CommentResponseDto dto = new CommentResponseDto();
        dto.setCommentId(comment.getId());
        dto.setUserId(comment.getUser().getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }
}