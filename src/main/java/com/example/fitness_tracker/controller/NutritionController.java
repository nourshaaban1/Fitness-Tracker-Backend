package com.example.fitness_tracker.controller;

import com.example.fitness_tracker.domain.dto.common.ErrorResponse;
import com.example.fitness_tracker.domain.dto.Nutrition.CreateNutritionDto;
import com.example.fitness_tracker.domain.dto.Nutrition.NutritionDto;
import com.example.fitness_tracker.domain.dto.Nutrition.UpdateNutritionDto;
import com.example.fitness_tracker.service.NutritionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/nutrition")
@RequiredArgsConstructor
public class NutritionController {

    private final NutritionService nutritionService;

    @GetMapping
    public ResponseEntity<List<NutritionDto>> getNutritions(
            @RequestParam(required = false, name = "name") String name,
            @RequestParam(required = false, name = "min_calories") Integer minCalories,
            @RequestParam(required = false, name = "max_calories") Integer maxCalories,
            Sort sort   // <-- e.g. ?sort=name,asc&sort=caloriesPer100g,desc
    ) {
        if (name != null || minCalories != null || maxCalories != null || sort.isSorted()) {
            return ResponseEntity.ok(
                    nutritionService.getFilteredAndSortedNutritions(name, minCalories, maxCalories, sort)
            );
        }
        return ResponseEntity.ok(nutritionService.getAll());
    }




    @GetMapping("/{id}")
    public ResponseEntity<NutritionDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(nutritionService.getById(id));
    }

    @PostMapping
    public ResponseEntity<NutritionDto> create(@RequestBody CreateNutritionDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(nutritionService.create(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<NutritionDto> update(@PathVariable UUID id,
                                               @RequestBody UpdateNutritionDto dto) {
        return ResponseEntity.ok(nutritionService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ErrorResponse> delete(@PathVariable UUID id) {
        nutritionService.delete(id);
        return ResponseEntity.ok(new ErrorResponse("Deleted successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<ErrorResponse> restore(@PathVariable UUID id) {
        nutritionService.restore(id);
        return ResponseEntity.ok(new ErrorResponse("Restored successfully", HttpStatus.OK.value()));
    }
}
