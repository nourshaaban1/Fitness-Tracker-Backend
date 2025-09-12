package com.example.fitness_tracker.controller;

import com.example.fitness_tracker.domain.dto.Workout.*;
import com.example.fitness_tracker.domain.dto.WorkoutExercise.CreateWorkoutExerciseDto;
import com.example.fitness_tracker.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;

    @GetMapping
    public ResponseEntity<List<WorkoutDto>> getWorkouts(
            @RequestParam(required = false, name = "name") String name,
            @RequestParam(required = false, name = "shared") Boolean shared,
            @RequestParam(required = false, name = "sort") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction
    ) {
        boolean hasFilter = (name != null && !name.isBlank()) || shared != null || sortBy != null;
        if (!hasFilter) {
            return ResponseEntity.ok(workoutService.getAll());
        }

        Sort sort = Sort.unsorted();
        if (sortBy != null) {
            sort = direction.equalsIgnoreCase("desc")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
        }

        return ResponseEntity.ok(workoutService.getFilteredAndSortedWorkouts(name, shared, sort));
    }


    @GetMapping("/{id}")
    public ResponseEntity<WorkoutDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(workoutService.getById(id));
    }



    @PostMapping
    public ResponseEntity<WorkoutDto> create(@RequestBody CreateWorkoutDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(workoutService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutDto> update(@PathVariable UUID id,
                                             @RequestBody UpdateWorkoutDto dto) {
        return ResponseEntity.ok(workoutService.update(id, dto));
    }

    @PostMapping("/{id}/exercises")
    public ResponseEntity<WorkoutDto> addExercises(
            @PathVariable UUID id,
            @RequestBody List<CreateWorkoutExerciseDto> exercises
    ) {
        return ResponseEntity.ok(workoutService.addExercisesToWorkout(id, exercises));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        workoutService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable UUID id) {
        workoutService.restore(id);
        return ResponseEntity.noContent().build();
    }
}
