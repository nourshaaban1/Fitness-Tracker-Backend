package com.example.fitness_tracker.domain.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.example.fitness_tracker.domain.enums.Role;
import com.example.fitness_tracker.domain.models.auditable.BaseEntityWithSoftDelete;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "users",
    indexes = {
        @Index(name = "idx_users_email", columnList = "email")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uc_users_email", columnNames = {"email"})
    }
)
public class User extends BaseEntityWithSoftDelete {

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "profile_pic", length = 512)
    private String profilePic;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Like> likes = new ArrayList<>();

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.USER;

    @Builder.Default
    @Column(name = "token_version")
    private Integer tokenVersion = 0;

    @Column(name = "last_login")
    private Instant lastLogin;

    @Column(name = "last_synced_at")
    private Instant lastSyncedAt;

    /** One-to-one relationship with preferences */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserPreferences preferences;

    /** One-to-many relationship with health metric */
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HealthMetric> healthMetrics = new ArrayList<>();
}