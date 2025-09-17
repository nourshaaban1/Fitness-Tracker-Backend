package com.example.fitness_tracker.domain.dto.OfflineEntry;

import com.example.fitness_tracker.domain.enums.SyncStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OfflineEntryResponseDto {
    private UUID entryId;
    private String dataType;
    private String dataContent;
    private SyncStatus syncStatus;
    private Instant createdAt;
    private Instant updatedAt;
}
