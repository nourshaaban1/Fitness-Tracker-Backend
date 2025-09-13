package com.example.fitness_tracker.domain.dto.Profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

import com.example.fitness_tracker.domain.enums.Theme;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String profilePic; // optional profile picture

    // Preferences
    private Theme theme;
    private Boolean notificationsEnabled;
    private LocalTime workoutReminderTime;
}
