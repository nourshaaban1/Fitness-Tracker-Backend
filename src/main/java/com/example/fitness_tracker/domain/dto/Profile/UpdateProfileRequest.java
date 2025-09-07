package com.example.fitness_tracker.domain.dto.Profile;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

import org.springframework.web.multipart.MultipartFile;

import com.example.fitness_tracker.domain.enums.Theme;

@Getter
@Setter
public class UpdateProfileRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private MultipartFile profilePic; // optional profile picture

    // Preferences
    private Theme theme;
    private Boolean notificationsEnabled;
    private LocalTime workoutReminderTime;
}
