package com.example.fitness_tracker.domain.dto.Profile;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateProfileResponse {
    private UserDto user; // a simplified User DTO
    private String newToken; // optional if email changed
}