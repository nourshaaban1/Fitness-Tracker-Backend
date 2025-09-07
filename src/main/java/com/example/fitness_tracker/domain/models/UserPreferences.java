package com.example.fitness_tracker.domain.models;

import java.time.LocalTime;

import com.example.fitness_tracker.domain.enums.Theme;
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
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "theme", nullable = false, length = 20)
    private Theme theme = Theme.LIGHT;

    @Builder.Default
    @Column(name = "notifications_enabled", nullable = false)
    private boolean notificationsEnabled = true;

    @Column(name = "workout_reminder_time")
    private LocalTime workoutReminderTime;
}