package com.example.fitness_tracker.domain.dto.WorkoutLog;

import com.example.fitness_tracker.domain.enums.SyncStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutLogDto {
    private UUID logId;
    private UUID userId;
    private UUID workoutId;
    private LocalDate date;
    private SyncStatus syncStatus;
}