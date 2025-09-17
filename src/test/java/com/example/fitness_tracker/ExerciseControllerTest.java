package com.example.fitness_tracker;

import com.example.fitness_tracker.controller.ExerciseController;
import com.example.fitness_tracker.domain.dto.Exercise.CreateExerciseDto;
import com.example.fitness_tracker.domain.dto.Exercise.ExerciseDto;
import com.example.fitness_tracker.domain.dto.Exercise.UpdateExerciseDto;
import com.example.fitness_tracker.domain.dto.common.ErrorResponse;
import com.example.fitness_tracker.service.ExerciseService;
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
class ExerciseControllerTest {

    @Mock
    private ExerciseService exerciseService;

    @InjectMocks
    private ExerciseController exerciseController;

    private ExerciseDto exerciseDto;
    private UUID exerciseId;

    @BeforeEach
    void setup() {
        exerciseId = UUID.randomUUID();
        exerciseDto = ExerciseDto.builder()
                .id(exerciseId)
                .name("Push Up")
                .build();
    }

    @Test
    void getExercises_withoutFilters_returnsAll() {
        when(exerciseService.getAll()).thenReturn(List.of(exerciseDto));

        ResponseEntity<List<ExerciseDto>> response = exerciseController.getExercises(null, null, null, "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(exerciseService).getAll();
        verify(exerciseService, never()).getFilteredAndSortedExercises(any(), any(), any());
    }

    @Test
    void getExercises_withFilters_callsFilteredService() {
        when(exerciseService.getFilteredAndSortedExercises(eq("Push"), eq("CHEST"), any(Sort.class)))
                .thenReturn(List.of(exerciseDto));

        ResponseEntity<List<ExerciseDto>> response = exerciseController.getExercises("Push", "CHEST", "name", "desc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(exerciseService).getFilteredAndSortedExercises(eq("Push"), eq("CHEST"), any(Sort.class));
    }

    @Test
    void getById_returnsExercise() {
        when(exerciseService.getById(exerciseId)).thenReturn(exerciseDto);

        ResponseEntity<ExerciseDto> response = exerciseController.getById(exerciseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(exerciseId, response.getBody().getId());
    }

    @Test
    void create_returnsCreated() {
        CreateExerciseDto createDto = new CreateExerciseDto();
        when(exerciseService.create(createDto)).thenReturn(exerciseDto);

        ResponseEntity<ExerciseDto> response = exerciseController.create(createDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(exerciseDto, response.getBody());
    }

    @Test
    void update_returnsOk() {
        UpdateExerciseDto updateDto = new UpdateExerciseDto();
        when(exerciseService.update(exerciseId, updateDto)).thenReturn(exerciseDto);

        ResponseEntity<ExerciseDto> response = exerciseController.update(exerciseId, updateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(exerciseDto, response.getBody());
    }

    @Test
    void delete_returnsMessage() {
        doNothing().when(exerciseService).delete(exerciseId);

        ResponseEntity<ErrorResponse> response = exerciseController.delete(exerciseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted successfully", response.getBody().getMessage());
    }

    @Test
    void restore_returnsMessage() {
        doNothing().when(exerciseService).restore(exerciseId);

        ResponseEntity<ErrorResponse> response = exerciseController.restore(exerciseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Restored successfully", response.getBody().getMessage());
    }
}


