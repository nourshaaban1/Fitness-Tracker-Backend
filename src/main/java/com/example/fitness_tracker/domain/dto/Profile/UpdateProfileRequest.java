package com.example.fitness_tracker.domain.dto.Profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

import org.springframework.web.multipart.MultipartFile;

import com.example.fitness_tracker.domain.enums.Theme;

@Getter
@Setter
public class UpdateProfileRequest {
    @Size(max = 100, message = "First name must be less than 100 characters")
    private String firstName;

    @Size(max = 100, message = "Last name must be less than 100 characters")
    private String lastName;

    @Email(message = "Invalid email format")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    private MultipartFile profilePic; // optional profile picture

    // Preferences
    private Theme theme;
    private Boolean notificationsEnabled;
    private LocalTime workoutReminderTime;
}