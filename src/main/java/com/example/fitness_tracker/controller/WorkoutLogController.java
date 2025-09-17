package com.example.fitness_tracker.controller;

import com.example.fitness_tracker.domain.dto.WorkoutLog.WorkoutLogDto;
import com.example.fitness_tracker.domain.dto.common.ErrorResponse;
import com.example.fitness_tracker.service.WorkoutLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/workout-logs")
@RequiredArgsConstructor
public class WorkoutLogController {

    private final WorkoutLogService service;

    // create new workout log
    @PostMapping
    public ResponseEntity<?> createLog(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody WorkoutLogDto dto,
            @RequestParam(value = "userId", required = false) UUID userId
    ) {
        try {
            return ResponseEntity.ok(service.createLog(authHeader, dto, userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage(), 400));
        }
    }

    // get specific log
    @GetMapping("/{logId}")
    public ResponseEntity<?> getLog(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID logId,
            @RequestParam(value = "userId", required = false) UUID userId
    ) {
        try {
            return ResponseEntity.ok(service.getLog(authHeader, logId, userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage(), 404));
        }
    }

    // get all logs for specific user
    @GetMapping("/user")
    public ResponseEntity<?> getAllForUser(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(value = "userId", required = false) UUID userId
    ) {
        return ResponseEntity.ok(service.getAllForUser(authHeader, userId));
    }

    // get all logs in DB (admin only)
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

    // update log (e.g. sync status or date)
    @PatchMapping("/{logId}")
    public ResponseEntity<?> updateLog(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID logId,
            @RequestBody WorkoutLogDto dto,
            @RequestParam(value = "userId", required = false) UUID userId
    ) {
        try {
            return ResponseEntity.ok(service.updateLog(authHeader, logId, dto, userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage(), 400));
        }
    }

    // soft delete log
    @DeleteMapping("/{logId}")
    public ResponseEntity<?> deleteLog(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID logId,
            @RequestParam(value = "userId", required = false) UUID userId
    ) {
        try {
            service.deleteLog(authHeader, logId, userId);
            return ResponseEntity.ok("Workout log deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage(), 404));
        }
    }
}