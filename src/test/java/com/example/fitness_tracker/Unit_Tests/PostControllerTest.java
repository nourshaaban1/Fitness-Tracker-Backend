package com.example.fitness_tracker.Unit_Tests;

import com.example.fitness_tracker.controller.PostController;
import com.example.fitness_tracker.domain.dto.Community.PostCreateDto;
import com.example.fitness_tracker.domain.dto.Community.PostResponseDto;
import com.example.fitness_tracker.domain.models.User;
import com.example.fitness_tracker.security.JwtService;
import com.example.fitness_tracker.service.AuthService;
import com.example.fitness_tracker.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    @Mock private JwtService jwtService;
    @Mock private PostService postService;
    @Mock private AuthService authService;

    @InjectMocks private PostController controller;

    private UUID userId;
    private UUID postId;

    @BeforeEach
    void setup() {
        userId = UUID.randomUUID();
        postId = UUID.randomUUID();
    }

    @Test
    void createPost_returnsResponse() {
        PostCreateDto dto = new PostCreateDto();
        PostResponseDto responseDto = new PostResponseDto();
        responseDto.setPostId(postId);
        when(jwtService.extractUserId("token")).thenReturn(userId);
        when(postService.createPost(eq(dto), any(User.class))).thenReturn(responseDto);

        ResponseEntity<PostResponseDto> response = controller.createPost(dto, "Bearer token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(postId, response.getBody().getPostId());
    }

    @Test
    void getAllPosts_returnsPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<PostResponseDto> page = new PageImpl<>(List.of(new PostResponseDto()));
        when(postService.getAllPosts(pageable)).thenReturn(page);

        ResponseEntity<Page<PostResponseDto>> response = controller.getAllPosts(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void getPost_returnsOne() {
        PostResponseDto dto = new PostResponseDto();
        dto.setPostId(postId);
        when(postService.getPostById(postId)).thenReturn(dto);

        ResponseEntity<PostResponseDto> response = controller.getPost(postId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(postId, response.getBody().getPostId());
    }

    @Test
    void deletePost_returnsNoContent() {
        when(jwtService.extractUserId("token")).thenReturn(userId);
        doNothing().when(postService).deletePost(eq(postId), any(User.class));

        ResponseEntity<Void> response = controller.deletePost(postId, "Bearer token");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(postService).deletePost(eq(postId), any(User.class));
    }
}


