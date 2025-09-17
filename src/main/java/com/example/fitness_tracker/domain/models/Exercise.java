package com.example.fitness_tracker.domain.models;

import com.example.fitness_tracker.domain.enums.ExerciseCategory;
import com.example.fitness_tracker.domain.enums.TrackingMode;
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
@Table(name = "exercises")
public class Exercise extends BaseEntityWithSoftDelete {

    @Column(nullable = false, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ExerciseCategory category;

    @Column(name = "image_url", length = 512)
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "tracking_mode", nullable = false, length = 20)
    private TrackingMode trackingMode;

    @Column(name = "has_weights", nullable = false)
    @Builder.Default
    private boolean hasWeights = false;

    /** User who created the exercise */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    /** Relations with workout exercises */
    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<WorkoutExercise> workoutExercises = new ArrayList<>();

}
