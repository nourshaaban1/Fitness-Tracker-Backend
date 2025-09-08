package com.example.fitness_tracker.domain.models;

import com.example.fitness_tracker.domain.enums.SyncStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfflineEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID entryId;  // <-- primary key

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String dataType;
    private String dataContent;
    private SyncStatus syncStatus;

    private Instant createdAt;
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }
}
