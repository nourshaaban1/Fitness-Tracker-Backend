package com.example.fitness_tracker.Unit_Tests;

import com.example.fitness_tracker.controller.NutritionController;
import com.example.fitness_tracker.domain.dto.Nutrition.CreateNutritionDto;
import com.example.fitness_tracker.domain.dto.Nutrition.NutritionDto;
import com.example.fitness_tracker.domain.dto.Nutrition.UpdateNutritionDto;
import com.example.fitness_tracker.domain.dto.common.ErrorResponse;
import com.example.fitness_tracker.service.NutritionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NutritionControllerTest {

    @Mock
    private NutritionService nutritionService;

    @InjectMocks
    private NutritionController nutritionController;

    private NutritionDto nutritionDto;
    private UUID nutritionId;

    @BeforeEach
    void setup() {
        nutritionId = UUID.randomUUID();
        nutritionDto = NutritionDto.builder()
                .id(nutritionId)
                .name("Chicken Breast")
                .build();
    }

    @Test
    void getNutritions_noFilters_returnsAll() {
        when(nutritionService.getAll()).thenReturn(List.of(nutritionDto));

        ResponseEntity<List<NutritionDto>> response = nutritionController.getNutritions(null, null, null, Sort.unsorted());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(nutritionService).getAll();
        verify(nutritionService, never()).getFilteredAndSortedNutritions(any(), any(), any(), any());
    }

    @Test
    void getNutritions_withFilters_callsFilteredService() {
        when(nutritionService.getFilteredAndSortedNutritions(eq("Chicken"), eq(100), eq(500), any(Sort.class)))
                .thenReturn(List.of(nutritionDto));

        ResponseEntity<List<NutritionDto>> response = nutritionController.getNutritions("Chicken", 100, 500, Sort.by("name").ascending());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(nutritionService).getFilteredAndSortedNutritions(eq("Chicken"), eq(100), eq(500), any(Sort.class));
    }

    @Test
    void getById_returnsNutrition() {
        when(nutritionService.getById(nutritionId)).thenReturn(nutritionDto);

        ResponseEntity<NutritionDto> response = nutritionController.getById(nutritionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(nutritionId, response.getBody().getId());
    }

    @Test
    void create_returnsCreated() {
        CreateNutritionDto createDto = new CreateNutritionDto();
        when(nutritionService.create(createDto)).thenReturn(nutritionDto);

        ResponseEntity<NutritionDto> response = nutritionController.create(createDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(nutritionDto, response.getBody());
    }

    @Test
    void update_returnsOk() {
        UpdateNutritionDto updateDto = new UpdateNutritionDto();
        when(nutritionService.update(nutritionId, updateDto)).thenReturn(nutritionDto);

        ResponseEntity<NutritionDto> response = nutritionController.update(nutritionId, updateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(nutritionDto, response.getBody());
    }

    @Test
    void delete_returnsMessage() {
        doNothing().when(nutritionService).delete(nutritionId);

        ResponseEntity<ErrorResponse> response = nutritionController.delete(nutritionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted successfully", response.getBody().getMessage());
    }

    @Test
    void restore_returnsMessage() {
        doNothing().when(nutritionService).restore(nutritionId);

        ResponseEntity<ErrorResponse> response = nutritionController.restore(nutritionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Restored successfully", response.getBody().getMessage());
    }
}


