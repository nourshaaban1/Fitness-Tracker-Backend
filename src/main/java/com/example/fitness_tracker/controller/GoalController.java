package com.example.fitness_tracker.controller;

import com.example.fitness_tracker.domain.dto.Goal.CreateGoalDto;
import com.example.fitness_tracker.domain.dto.Goal.GoalDto;
import com.example.fitness_tracker.domain.dto.Goal.UpdateGoalDto;
import com.example.fitness_tracker.domain.dto.common.ErrorResponse;
import com.example.fitness_tracker.service.GoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    @GetMapping
    public ResponseEntity<List<GoalDto>> getAllGoalsForCurrentUser() {
        return ResponseEntity.ok(goalService.getAllGoalsForCurrentUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GoalDto> getGoalByIdForCurrentUser(@PathVariable UUID id){
        return ResponseEntity.ok(goalService.getGoalByIdForCurrentUser(id));
    }

    @PostMapping
    public ResponseEntity<GoalDto> createGoal(@Valid @RequestBody CreateGoalDto dto) {
        GoalDto created = goalService.createGoal(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GoalDto> updateGoal(@PathVariable UUID id,
                                              @Valid @RequestBody UpdateGoalDto dto) {
        return ResponseEntity.ok(goalService.updateGoal(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ErrorResponse> deleteGoal(@PathVariable UUID id) {
        goalService.delete(id);
        return ResponseEntity.ok(new ErrorResponse("Deleted Successfully", HttpStatus.OK.value()));
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<ErrorResponse> restoreGoal(@PathVariable UUID id) {
        goalService.restore(id);
        return ResponseEntity.ok(new ErrorResponse("Restored Successfully", HttpStatus.OK.value()));
    }
}
