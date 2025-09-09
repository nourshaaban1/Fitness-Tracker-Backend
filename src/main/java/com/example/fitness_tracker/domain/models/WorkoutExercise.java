package com.example.fitness_tracker.domain.models;

import com.example.fitness_tracker.domain.models.auditable.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "workout_exercises")
public class WorkoutExercise extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column
    private Integer sets;

    @Column
    private Integer reps;

    @Column
    private Integer duration; // in seconds

    @Column
    private Integer calories;

    @Column(name = "order_in_workout")
    private Integer orderInWorkout;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
