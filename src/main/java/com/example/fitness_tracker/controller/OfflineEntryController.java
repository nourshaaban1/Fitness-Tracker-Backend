package com.example.fitness_tracker.controller;

import com.example.fitness_tracker.domain.dto.OfflineEntry.OfflineEntryRequestDto;
import com.example.fitness_tracker.domain.dto.OfflineEntry.OfflineEntryResponseDto;
import com.example.fitness_tracker.security.JwtService;
import com.example.fitness_tracker.service.OfflineEntryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/offline-entries")
@RequiredArgsConstructor
public class OfflineEntryController {

    private final OfflineEntryService offlineEntryService;
    private final JwtService jwtService;

    @GetMapping
    public ResponseEntity<List<OfflineEntryResponseDto>> getAllOfflineEntries(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        UUID userId = jwtService.extractUserId(token);

        return ResponseEntity.ok(
                offlineEntryService.getAllOfflineEntries(userId)
        );
    }

    @GetMapping("/{entryId}")
    public ResponseEntity<OfflineEntryResponseDto> getOfflineEntryById(
            @PathVariable UUID entryId,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        UUID userId = jwtService.extractUserId(token);

        return ResponseEntity.ok(
                offlineEntryService.getOfflineEntryById(entryId, userId)
        );
    }

    @PostMapping
    public ResponseEntity<OfflineEntryResponseDto> createOfflineEntry(
            @Valid @RequestBody OfflineEntryRequestDto requestDto,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        UUID userId = jwtService.extractUserId(token);
        requestDto.setUserId(userId);
        return new ResponseEntity<>(
                offlineEntryService.createOfflineEntry(requestDto),
                HttpStatus.CREATED
        );
    }


    @PutMapping("/{entryId}")
    public ResponseEntity<OfflineEntryResponseDto> updateOfflineEntry(
            @PathVariable UUID entryId,
            @Valid @RequestBody OfflineEntryRequestDto requestDto,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        UUID userId = jwtService.extractUserId(token);
        requestDto.setUserId(userId);
        return ResponseEntity.ok(
                offlineEntryService.updateOfflineEntry(entryId, requestDto)
        );
    }


    @DeleteMapping("/{entryId}")
    public ResponseEntity<Void> deleteOfflineEntry(
            @PathVariable UUID entryId,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        UUID userId = jwtService.extractUserId(token);

        offlineEntryService.deleteOfflineEntry(entryId, userId);
        return ResponseEntity.noContent().build();
    }
}
