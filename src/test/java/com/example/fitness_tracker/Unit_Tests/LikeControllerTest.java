package com.example.fitness_tracker.Unit_Tests;

import com.example.fitness_tracker.controller.LikeController;
import com.example.fitness_tracker.security.JwtService;
import com.example.fitness_tracker.service.AuthService;
import com.example.fitness_tracker.service.LikeService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeControllerTest {

    @Mock private JwtService jwtService;
    @Mock private LikeService likeService;
    @Mock private AuthService authService;

    @InjectMocks private LikeController controller;

    private UUID postId;
    private UUID userId;

    @BeforeEach
    void setup() {
        postId = UUID.randomUUID();
        userId = UUID.randomUUID();
        when(jwtService.extractUserId("token")).thenReturn(userId);
    }

    @Test
    void likePost_returnsOk() {
        doNothing().when(likeService).likePost(postId, userId);

        ResponseEntity<Void> response = controller.likePost(postId, "Bearer token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(likeService).likePost(postId, userId);
    }

    @Test
    void unlikePost_returnsNoContent() {
        doNothing().when(likeService).unlikePost(postId, userId);

        ResponseEntity<Void> response = controller.unlikePost(postId, "Bearer token");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(likeService).unlikePost(postId, userId);
    }
}


