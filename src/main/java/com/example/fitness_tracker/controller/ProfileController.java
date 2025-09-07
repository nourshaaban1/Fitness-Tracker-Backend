package com.example.fitness_tracker.controller;

import com.example.fitness_tracker.domain.dto.Profile.UpdateProfileRequest;
import com.example.fitness_tracker.domain.dto.Profile.UpdateProfileResponse;
import com.example.fitness_tracker.domain.dto.Profile.UserDto;
import com.example.fitness_tracker.domain.dto.common.ErrorResponse;
import com.example.fitness_tracker.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String authHeader) {
        try {
            UserDto profile = profileService.getProfile(authHeader);
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
            @ModelAttribute UpdateProfileRequest request
    ) {
        try {
            UpdateProfileResponse response = profileService.updateProfile(authHeader, request);
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
    public ResponseEntity<?> deleteProfile(@RequestHeader("Authorization") String authHeader) {
        try {
            profileService.deleteProfile(authHeader);
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