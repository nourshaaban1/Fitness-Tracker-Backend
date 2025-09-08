package com.example.fitness_tracker.service;

import com.example.fitness_tracker.domain.dto.OfflineEntry.OfflineEntryRequestDto;
import com.example.fitness_tracker.domain.dto.OfflineEntry.OfflineEntryResponseDto;
import com.example.fitness_tracker.domain.models.OfflineEntry;
import com.example.fitness_tracker.domain.models.User;
import com.example.fitness_tracker.exceptions.ResourceNotFoundException;
import com.example.fitness_tracker.repository.OfflineEntryRepository;
import com.example.fitness_tracker.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Getter
@RequiredArgsConstructor
public class OfflineEntryService {

    private final OfflineEntryRepository offlineEntryRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<OfflineEntryResponseDto> getAllOfflineEntries(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return offlineEntryRepository.findByUser(user).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OfflineEntryResponseDto getOfflineEntryById(UUID entryId, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        OfflineEntry entry = offlineEntryRepository.findByEntryIdAndUser(entryId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Offline entry not found with id: " + entryId));

        return convertToDto(entry);
    }

    @Transactional
    public OfflineEntryResponseDto createOfflineEntry(OfflineEntryRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + requestDto.getUserId()));

        OfflineEntry entry = OfflineEntry.builder()
                .user(user)
                .dataType(requestDto.getDataType())
                .dataContent(requestDto.getDataContent())
                .syncStatus(requestDto.getSyncStatus())
                .build();

        OfflineEntry savedEntry = offlineEntryRepository.save(entry);
        return convertToDto(savedEntry);
    }

    @Transactional
    public OfflineEntryResponseDto updateOfflineEntry(UUID entryId, OfflineEntryRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + requestDto.getUserId()));

        OfflineEntry existingEntry = offlineEntryRepository.findByEntryIdAndUser(entryId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Offline entry not found with id: " + entryId));

        existingEntry.setDataType(requestDto.getDataType());
        existingEntry.setDataContent(requestDto.getDataContent());
        existingEntry.setSyncStatus(requestDto.getSyncStatus());

        OfflineEntry updatedEntry = offlineEntryRepository.save(existingEntry);
        return convertToDto(updatedEntry);
    }

    @Transactional
    public void deleteOfflineEntry(UUID entryId, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        offlineEntryRepository.deleteByEntryIdAndUser(entryId, user);
    }

    private OfflineEntryResponseDto convertToDto(OfflineEntry entry) {
        return OfflineEntryResponseDto.builder()
                .entryId(entry.getEntryId())
//                .userId(entry.getUser().getUserId())
                .dataType(entry.getDataType())
                .dataContent(entry.getDataContent())
                .syncStatus(entry.getSyncStatus())
                .createdAt(entry.getCreatedAt())
                .updatedAt(entry.getUpdatedAt())
                .build();
    }
}
