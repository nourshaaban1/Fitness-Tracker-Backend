package com.example.fitness_tracker.controller;

import com.example.fitness_tracker.domain.dto.HealthMetric.HealthMetricDto;
import com.example.fitness_tracker.domain.dto.common.ErrorResponse;
import com.example.fitness_tracker.service.HealthMetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/health-metrics")
@RequiredArgsConstructor
public class HealthMetricController {

    private final HealthMetricService service;

    // create a new health metric
    @PostMapping
    public ResponseEntity<?> createMetric(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody HealthMetricDto dto,
            @RequestParam(value = "userId", required = false) UUID userId
    ) {
        try {
            return ResponseEntity.ok(service.createMetric(authHeader, dto, userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage(), 400));
        }
    }

    // get specific health metric
    @GetMapping("/{metricId}")
    public ResponseEntity<?> getMetric(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID metricId,
            @RequestParam(value = "userId", required = false) UUID userId
    ) {
        try {
            return ResponseEntity.ok(service.getMetric(authHeader, metricId, userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage(), 404));
        }
    }

    // get all metrics for specific user
    @GetMapping("/user")
    public ResponseEntity<?> getAllForUser(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(value = "userId", required = false) UUID userId
    ) {
        return ResponseEntity.ok(service.getAllForUser(authHeader, userId));
    }

    // get all metrics in DB (admin only)
    @GetMapping("/all")
    public ResponseEntity<?> getAll(
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            return ResponseEntity.ok(service.getAll(authHeader));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(e.getMessage(), 401));
        }
    }

    // update specific metric
    @PatchMapping("/{metricId}")
    public ResponseEntity<?> updateMetric(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID metricId,
            @RequestBody HealthMetricDto dto,
            @RequestParam(value = "userId", required = false) UUID userId
    ) {
        try {
            return ResponseEntity.ok(service.updateMetric(authHeader, metricId, dto, userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage(), 400));
        }
    }

    // delete metric (soft delete)
    @DeleteMapping("/{metricId}")
    public ResponseEntity<?> deleteMetric(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID metricId,
            @RequestParam(value = "userId", required = false) UUID userId
    ) {
        try {
            service.deleteMetric(authHeader, metricId, userId);
            return ResponseEntity.ok("Metric deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage(), 404));
        }
    }
}