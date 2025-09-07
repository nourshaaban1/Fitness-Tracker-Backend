package com.example.fitness_tracker.controller;

import com.example.fitness_tracker.domain.dto.Profile.UpdateProfileRequest;
import com.example.fitness_tracker.domain.dto.Profile.UpdateProfileResponse;
import com.example.fitness_tracker.domain.dto.Profile.UserDto;
import com.example.fitness_tracker.domain.dto.common.ErrorResponse;
import com.example.fitness_tracker.service.ProfileService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    // Admin-only endpoint to get all users
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')") // Only users with ADMIN role can access
    public ResponseEntity<?> getAllUsers(
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            var users = profileService.getAllUsers(authHeader);
            return ResponseEntity.ok(users);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(e.getMessage(), 401));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Server error", 500));
        }
    }

    @GetMapping
    public ResponseEntity<?> getProfile(
        @RequestHeader("Authorization") String authHeader,
        @RequestParam(value = "userId", required = false) UUID userId
    ) {
        try {
            UserDto profile = profileService.getProfile(authHeader, userId);
            return ResponseEntity.ok(profile);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(e.getMessage(), 401));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Server error", 500));
        }
    }

    @PatchMapping(value = "/update", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(value = "userId", required = false) UUID userId,
            @ModelAttribute UpdateProfileRequest request
    ) {
        try {
            UpdateProfileResponse response = profileService.updateProfile(authHeader, request, userId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage(), 400));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(e.getMessage(), 401));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Server error", 500));
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteProfile(
        @RequestHeader("Authorization") String authHeader,
        @RequestParam(value = "userId", required = false) UUID userId
    ) {
        try {
            profileService.deleteProfile(authHeader, userId);
            return ResponseEntity.ok().body("Profile deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(e.getMessage(), 401));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Server error", 500));
        }
    }
}