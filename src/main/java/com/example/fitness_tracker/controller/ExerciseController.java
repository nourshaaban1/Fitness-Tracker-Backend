package com.example.fitness_tracker.controller;

import com.example.fitness_tracker.domain.dto.Exercise.CreateExerciseDto;
import com.example.fitness_tracker.domain.dto.Exercise.ExerciseDto;
import com.example.fitness_tracker.domain.dto.Exercise.UpdateExerciseDto;
import com.example.fitness_tracker.domain.dto.common.ErrorResponse;
import com.example.fitness_tracker.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
@Validated
public class ExerciseController {

    private final ExerciseService exerciseService;

    @GetMapping
    public ResponseEntity<List<ExerciseDto>> getExercises(
            @RequestParam(required = false) String name,
            @RequestParam(required = false, name = "category") String category,
            @RequestParam(required = false, name = "sort_by") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction
    ) {
        boolean hasFilter = (name != null && !name.isBlank()) || (category != null && !category.isBlank()) || sortBy != null;

        if (!hasFilter) {
            return ResponseEntity.ok(exerciseService.getAll());
        }

        Sort sort = Sort.unsorted();
        if (sortBy != null) {
            sort = direction.equalsIgnoreCase("desc")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
        }

        return ResponseEntity.ok(
                exerciseService.getFilteredAndSortedExercises(name, category, sort)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(exerciseService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ExerciseDto> create(@Valid @RequestBody CreateExerciseDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(exerciseService.create(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ExerciseDto> update(@PathVariable UUID id,
                                              @Valid @RequestBody UpdateExerciseDto dto) {
        return ResponseEntity.ok(exerciseService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ErrorResponse> delete(@PathVariable UUID id) {
        exerciseService.delete(id);
        return ResponseEntity.ok(new ErrorResponse("Deleted successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<ErrorResponse> restore(@PathVariable UUID id) {
        exerciseService.restore(id);
        return ResponseEntity.ok(new ErrorResponse("Restored successfully", HttpStatus.OK.value()));
    }
}
