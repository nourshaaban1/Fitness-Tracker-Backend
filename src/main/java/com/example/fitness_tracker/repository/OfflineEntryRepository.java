package com.example.fitness_tracker.repository;

import com.example.fitness_tracker.domain.models.OfflineEntry;
import com.example.fitness_tracker.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OfflineEntryRepository extends JpaRepository<OfflineEntry, UUID> {
    List<OfflineEntry> findByUser(User user);
    Optional<OfflineEntry> findByEntryIdAndUser(UUID entryId, User user);
    void deleteByEntryIdAndUser(UUID entryId, User user);
}
