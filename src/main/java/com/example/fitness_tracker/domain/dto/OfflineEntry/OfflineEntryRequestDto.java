package com.example.fitness_tracker.domain.dto.OfflineEntry;

import com.example.fitness_tracker.domain.enums.SyncStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OfflineEntryRequestDto {
    private UUID userId;
    private String dataType;
    private String dataContent;
    private SyncStatus syncStatus;
}
