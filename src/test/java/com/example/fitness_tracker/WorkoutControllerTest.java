package com.example.fitness_tracker;

import com.example.fitness_tracker.controller.WorkoutController;
import com.example.fitness_tracker.domain.dto.Workout.CreateWorkoutDto;
import com.example.fitness_tracker.domain.dto.Workout.UpdateWorkoutDto;
import com.example.fitness_tracker.domain.dto.Workout.WorkoutDto;
import com.example.fitness_tracker.domain.dto.WorkoutExercise.CreateWorkoutExerciseDto;
import com.example.fitness_tracker.service.WorkoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutControllerTest {

    @Mock
    private WorkoutService workoutService;

    @InjectMocks
    private WorkoutController workoutController;

    private WorkoutDto workoutDto;
    private UUID workoutId;

    @BeforeEach
    void setup() {
        workoutId = UUID.randomUUID();
        workoutDto = WorkoutDto.builder()
                .id(workoutId)
                .name("Full Body")
                .build();
    }

    @Test
    void getWorkouts_withoutFilters_returnsAll() {
        when(workoutService.getAll()).thenReturn(List.of(workoutDto));

        ResponseEntity<List<WorkoutDto>> response = workoutController.getWorkouts(null, null, null, "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(workoutService).getAll();
        verify(workoutService, never()).getFilteredAndSortedWorkouts(any(), any(), any());
    }

    @Test
    void getWorkouts_withFilters_callsFilteredService() {
        when(workoutService.getFilteredAndSortedWorkouts(eq("Full"), eq(true), any()))
                .thenReturn(List.of(workoutDto));

        ResponseEntity<List<WorkoutDto>> response = workoutController.getWorkouts("Full", true, "name", "desc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(workoutService).getFilteredAndSortedWorkouts(eq("Full"), eq(true), any());
    }

    @Test
    void getById_returnsWorkout() {
        when(workoutService.getById(workoutId)).thenReturn(workoutDto);

        ResponseEntity<WorkoutDto> response = workoutController.getById(workoutId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(workoutId, response.getBody().getId());
    }

    @Test
    void create_returnsCreated() {
        CreateWorkoutDto createDto = new CreateWorkoutDto();
        when(workoutService.create(createDto)).thenReturn(workoutDto);

        ResponseEntity<WorkoutDto> response = workoutController.create(createDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(workoutDto, response.getBody());
    }

    @Test
    void update_returnsOk() {
        UpdateWorkoutDto updateDto = new UpdateWorkoutDto();
        when(workoutService.update(workoutId, updateDto)).thenReturn(workoutDto);

        ResponseEntity<WorkoutDto> response = workoutController.update(workoutId, updateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(workoutDto, response.getBody());
    }

    @Test
    void addExercises_returnsOk() {
        List<CreateWorkoutExerciseDto> items = List.of(new CreateWorkoutExerciseDto());
        when(workoutService.addExercisesToWorkout(workoutId, items)).thenReturn(workoutDto);

        ResponseEntity<WorkoutDto> response = workoutController.addExercises(workoutId, items);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(workoutDto, response.getBody());
    }

    @Test
    void delete_returnsNoContent() {
        doNothing().when(workoutService).delete(workoutId);

        ResponseEntity<Void> response = workoutController.delete(workoutId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void restore_returnsNoContent() {
        doNothing().when(workoutService).restore(workoutId);

        ResponseEntity<Void> response = workoutController.restore(workoutId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}


