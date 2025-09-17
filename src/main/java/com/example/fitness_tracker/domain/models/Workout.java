package com.example.fitness_tracker.domain.models;

import com.example.fitness_tracker.domain.models.auditable.BaseEntityWithSoftDelete;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "workouts")
public class Workout extends BaseEntityWithSoftDelete {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_shared", nullable = false)
    @Builder.Default
    private boolean isShared = false;

    /** User who created this workout */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    /** Exercises in this workout */
    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<WorkoutExercise> workoutExercises = new ArrayList<>();
}
