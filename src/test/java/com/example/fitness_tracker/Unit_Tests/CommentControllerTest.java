package com.example.fitness_tracker.Unit_Tests;

import com.example.fitness_tracker.controller.CommentController;
import com.example.fitness_tracker.domain.dto.Community.CommentCreateDto;
import com.example.fitness_tracker.domain.dto.Community.CommentResponseDto;
import com.example.fitness_tracker.security.JwtService;
import com.example.fitness_tracker.service.AuthService;
import com.example.fitness_tracker.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @Mock private JwtService jwtService;
    @Mock private CommentService commentService;
    @Mock private AuthService authService;

    @InjectMocks private CommentController controller;

    private UUID postId;
    private UUID userId;
    private UUID commentId;

    @BeforeEach
    void setup() {
        postId = UUID.randomUUID();
        userId = UUID.randomUUID();
        commentId = UUID.randomUUID();
        when(jwtService.extractUserId("token"))
                .thenReturn(userId);
    }

    @Test
    void createComment_returnsResponse() {
        CommentCreateDto dto = new CommentCreateDto();
        CommentResponseDto responseDto = new CommentResponseDto();
        responseDto.setCommentId(commentId);
        when(commentService.createComment(eq(postId), eq(dto), eq(userId)))
                .thenReturn(responseDto);

        ResponseEntity<CommentResponseDto> response = controller.createComment(postId, dto, "Bearer token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(commentId, response.getBody().getCommentId());
    }

    @Test
    void deleteComment_returnsNoContent() {
        doNothing().when(commentService).deleteComment(eq(commentId), eq(userId));

        ResponseEntity<Void> response = controller.deleteComment(commentId, "Bearer token");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(commentService).deleteComment(commentId, userId);
    }
}


