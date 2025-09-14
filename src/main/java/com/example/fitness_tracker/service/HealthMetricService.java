package com.example.fitness_tracker.service;

import com.example.fitness_tracker.domain.dto.HealthMetric.HealthMetricDto;
import com.example.fitness_tracker.domain.models.HealthMetric;
import com.example.fitness_tracker.domain.models.User;
import com.example.fitness_tracker.repository.HealthMetricRepository;
import com.example.fitness_tracker.repository.UserRepository;
import com.example.fitness_tracker.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HealthMetricService {

    private final JwtService jwtService;
    private final HealthMetricRepository healthMetricRepository;
    private final UserRepository userRepository;

    // Create health metric
    public HealthMetricDto createMetric(String authHeader, HealthMetricDto dto, UUID targetUserId) {
        UUID userId = getValidUserId(authHeader, targetUserId);
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        HealthMetric metric = HealthMetric.builder()
                .user(user)
                .weight(dto.getWeight())
                .height(dto.getHeight())
                .bmi(0f) // auto-calculated in entity
                .bodyMeasurements(dto.getBodyMeasurements())
                .date(dto.getDate())
                .build();

        HealthMetric saved = healthMetricRepository.save(metric);
        return mapToDto(saved);
    }

    // Get specific health metric
    public HealthMetricDto getMetric(String authHeader, UUID metricId, UUID targetUserId) {
        UUID userId = getValidUserId(authHeader, targetUserId);

        HealthMetric metric = healthMetricRepository.findByIdAndUserIdAndDeletedAtIsNull(metricId, userId)
                .orElseThrow(() -> new RuntimeException("Metric not found"));

        return mapToDto(metric);
    }

    // Get all metrics for specific user
    public List<HealthMetricDto> getAllForUser(String authHeader, UUID targetUserId) {
        UUID userId = getValidUserId(authHeader, targetUserId);
        return healthMetricRepository.findAllByUserIdAndDeletedAtIsNull(userId)
                .stream().map(this::mapToDto).toList();
    }

    // Get all metrics in DB (admin only)
    public List<HealthMetricDto> getAll(String authHeader) {
        validateAdmin(authHeader);
        return healthMetricRepository.findAllByDeletedAtIsNull()
                .stream().map(this::mapToDto).toList();
    }

    // Update specific metric
    public HealthMetricDto updateMetric(String authHeader, UUID metricId, HealthMetricDto dto, UUID targetUserId) {
        UUID userId = getValidUserId(authHeader, targetUserId);

        HealthMetric metric = healthMetricRepository.findByIdAndUserIdAndDeletedAtIsNull(metricId, userId)
                .orElseThrow(() -> new RuntimeException("Metric not found"));

        if (dto.getWeight() > 0) metric.setWeight(dto.getWeight());
        if (dto.getHeight() > 0) metric.setHeight(dto.getHeight());
        if (dto.getBodyMeasurements() != null) metric.setBodyMeasurements(dto.getBodyMeasurements());
        if (dto.getDate() != null) metric.setDate(dto.getDate());

        HealthMetric updated = healthMetricRepository.save(metric);
        return mapToDto(updated);
    }

    // Delete metric (soft delete)
    public void deleteMetric(String authHeader, UUID metricId, UUID targetUserId) {
        UUID userId = getValidUserId(authHeader, targetUserId);

        HealthMetric metric = healthMetricRepository.findByIdAndUserIdAndDeletedAtIsNull(metricId, userId)
                .orElseThrow(() -> new RuntimeException("Metric not found"));

        metric.softDelete();
        healthMetricRepository.save(metric);
    }

    // Helper: map entity to DTO
    private HealthMetricDto mapToDto(HealthMetric metric) {
        return new HealthMetricDto(
                metric.getId(),
                metric.getWeight(),
                metric.getHeight(),
                metric.getBmi(),
                metric.getBodyMeasurements(),
                metric.getDate()
        );
    }

    // Helper: validate user/admin access
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
}