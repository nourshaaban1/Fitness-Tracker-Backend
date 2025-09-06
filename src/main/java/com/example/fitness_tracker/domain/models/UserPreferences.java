package com.example.fitness_tracker.domain.models;

import com.example.fitness_tracker.domain.models.auditable.BaseEntityWithSoftDelete;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_preferences")
public class UserPreferences extends BaseEntityWithSoftDelete {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    /** Example preferences (can expand later) */
    @Column(name = "theme", nullable = false, length = 20)
    @Builder.Default
    private String theme = "light"; // default theme

    @Column(name = "notifications_enabled", nullable = false)
    @Builder.Default
    private boolean notificationsEnabled = true;

    @Column(name = "workout_reminder_time")
    private java.time.LocalTime workoutReminderTime;
}