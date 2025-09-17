package com.example.fitness_tracker.domain.models;

import com.example.fitness_tracker.domain.enums.SyncStatus;
import com.example.fitness_tracker.domain.models.auditable.BaseEntityWithSoftDelete;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Table(name = "workout_logs")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutLog extends BaseEntityWithSoftDelete {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private SyncStatus syncStatus = SyncStatus.PENDING;

}
