package com.example.fitness_tracker.service;

import com.example.fitness_tracker.domain.dto.WorkoutLog.WorkoutLogDto;
import com.example.fitness_tracker.domain.enums.SyncStatus;
import com.example.fitness_tracker.domain.models.User;
import com.example.fitness_tracker.domain.models.Workout;
import com.example.fitness_tracker.domain.models.WorkoutLog;
import com.example.fitness_tracker.repository.UserRepository;
import com.example.fitness_tracker.repository.WorkoutLogRepository;
import com.example.fitness_tracker.repository.WorkoutRepository;
import com.example.fitness_tracker.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkoutLogService {

    private final JwtService jwtService;
    private final WorkoutLogRepository logRepository;
    private final UserRepository userRepository;
    private final WorkoutRepository workoutRepository;

    // create log
    public WorkoutLogDto createLog(String authHeader, WorkoutLogDto dto, UUID targetUserId) {
        UUID userId = getValidUserId(authHeader, targetUserId);
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Workout workout = workoutRepository.findByIdAndDeletedAtIsNull(dto.getWorkoutId())
                .orElseThrow(() -> new RuntimeException("Workout not found"));

        WorkoutLog log = WorkoutLog.builder()
                .user(user)
                .workout(workout)
                .date(dto.getDate())
                .syncStatus(dto.getSyncStatus() != null ? dto.getSyncStatus() : SyncStatus.PENDING)
                .build();

        return mapToDto(logRepository.save(log));
    }

    // get specific log
    public WorkoutLogDto getLog(String authHeader, UUID logId, UUID targetUserId) {
        UUID userId = getValidUserId(authHeader, targetUserId);
        WorkoutLog log = logRepository.findByIdAndUserIdAndDeletedAtIsNull(logId, userId)
                .orElseThrow(() -> new RuntimeException("Workout log not found"));
        return mapToDto(log);
    }

    // get all logs for user
    public List<WorkoutLogDto> getAllForUser(String authHeader, UUID targetUserId) {
        UUID userId = getValidUserId(authHeader, targetUserId);
        return logRepository.findAllByUserIdAndDeletedAtIsNull(userId)
                .stream().map(this::mapToDto).toList();
    }

    // get all logs (admin only)
    public List<WorkoutLogDto> getAll(String authHeader) {
        validateAdmin(authHeader);
        return logRepository.findAllByDeletedAtIsNull()
                .stream().map(this::mapToDto).toList();
    }

    // update log
    public WorkoutLogDto updateLog(String authHeader, UUID logId, WorkoutLogDto dto, UUID targetUserId) {
        UUID userId = getValidUserId(authHeader, targetUserId);
        WorkoutLog log = logRepository.findByIdAndUserIdAndDeletedAtIsNull(logId, userId)
                .orElseThrow(() -> new RuntimeException("Workout log not found"));

        if (dto.getDate() != null) log.setDate(dto.getDate());
        if (dto.getSyncStatus() != null) log.setSyncStatus(dto.getSyncStatus());

        return mapToDto(logRepository.save(log));
    }

    // delete log (soft delete)
    public void deleteLog(String authHeader, UUID logId, UUID targetUserId) {
        UUID userId = getValidUserId(authHeader, targetUserId);
        WorkoutLog log = logRepository.findByIdAndUserIdAndDeletedAtIsNull(logId, userId)
                .orElseThrow(() -> new RuntimeException("Workout log not found"));
        log.softDelete();
        logRepository.save(log);
    }

    // ===== helper methods =====

    private UUID getValidUserId(String authHeader, UUID targetUserId) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        UUID requesterId = jwtService.extractUserId(token);
        String role = jwtService.extractRole(token);

        if ("ADMIN".equalsIgnoreCase(role) && targetUserId != null) {
            return targetUserId;
        }
        return requesterId;
    }

    private void validateAdmin(String authHeader) {
        String token = authHeader.substring(7);
        String role = jwtService.extractRole(token);
        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new RuntimeException("Admins only");
        }
    }

    private WorkoutLogDto mapToDto(WorkoutLog log) {
        return WorkoutLogDto.builder()
                .logId(log.getId())
                .userId(log.getUser().getId())
                .workoutId(log.getWorkout().getId())
                .date(log.getDate())
                .syncStatus(log.getSyncStatus())
                .build();
    }
}