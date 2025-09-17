package com.example.fitness_tracker.domain.models;

import com.example.fitness_tracker.domain.enums.GoalStatus;
import com.example.fitness_tracker.domain.models.auditable.BaseEntityWithSoftDelete;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "goals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Goal extends BaseEntityWithSoftDelete {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String description;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;

    @Column(name = "target_weight")
    private Double targetWeight;

    @Column(name = "current_weight")
    @Builder.Default
    private Double currentWeight = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private GoalStatus status = GoalStatus.NOT_STARTED;

}