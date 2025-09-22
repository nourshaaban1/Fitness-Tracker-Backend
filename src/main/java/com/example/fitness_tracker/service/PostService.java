package com.example.fitness_tracker.service;

import com.example.fitness_tracker.domain.models.Post;
import com.example.fitness_tracker.domain.models.User;
import com.example.fitness_tracker.domain.dto.Community.PostCreateDto;
import com.example.fitness_tracker.domain.dto.Community.PostResponseDto;
import com.example.fitness_tracker.exceptions.NotFoundException;
import com.example.fitness_tracker.exceptions.UnauthorizedException;
import com.example.fitness_tracker.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AuthService authService;

    public PostResponseDto createPost(PostCreateDto dto, User currentUser) {
        Post post = new Post();
        post.setUser(currentUser);
        post.setContent(dto.getContent());
        post.setImageUrl(dto.getImageUrl());
        post = postRepository.save(post);
        return mapToResponseDto(post);
    }

    public Page<PostResponseDto> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable).map(this::mapToResponseDto);
    }

    public PostResponseDto getPostById(UUID id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post not found"));
        return mapToResponseDto(post);
    }

    public void deletePost(UUID id, User currentUser) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post not found"));
        if (!post.getUser().getId().equals(currentUser.getId()) && !"ADMIN".equals(currentUser.getRole())) {
            throw new UnauthorizedException("Not authorized to delete this post");
        }
        postRepository.delete(post);
    }

    private PostResponseDto mapToResponseDto(Post post) {
        PostResponseDto dto = new PostResponseDto();
        dto.setPostId(post.getId());
        dto.setUserId(post.getUser().getId());
        dto.setContent(post.getContent());
        dto.setImageUrl(post.getImageUrl());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setLikeCount(post.getLikes().size());
        dto.setCommentCount(post.getComments().size());
        return dto;
    }
}