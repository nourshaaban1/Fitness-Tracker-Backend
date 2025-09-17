package com.example.fitness_tracker;

import com.example.fitness_tracker.controller.WorkoutExerciseController;
import com.example.fitness_tracker.domain.dto.WorkoutExercise.CreateWorkoutExerciseDto;
import com.example.fitness_tracker.domain.dto.WorkoutExercise.WorkoutExerciseDto;
import com.example.fitness_tracker.service.WorkoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutExerciseControllerTest {

    @Mock private WorkoutService workoutService;
    @InjectMocks private WorkoutExerciseController controller;

    private UUID workoutId;
    private UUID exerciseId;

    @BeforeEach
    void setup() {
        workoutId = UUID.randomUUID();
        exerciseId = UUID.randomUUID();
    }

    @Test
    void getWorkoutExercises_returnsList() {
        when(workoutService.getWorkoutExercises(workoutId)).thenReturn(List.of(new WorkoutExerciseDto()));
        List<WorkoutExerciseDto> list = controller.getWorkoutExercises(workoutId);
        assertEquals(1, list.size());
    }

    @Test
    void getWorkoutExercise_returnsOne() {
        WorkoutExerciseDto dto = new WorkoutExerciseDto();
        when(workoutService.getWorkoutExercise(exerciseId)).thenReturn(dto);
        WorkoutExerciseDto result = controller.getWorkoutExercise(workoutId, exerciseId);
        assertSame(dto, result);
    }

    @Test
    void updateWorkoutExercise_returnsUpdated() {
        CreateWorkoutExerciseDto req = new CreateWorkoutExerciseDto();
        WorkoutExerciseDto dto = new WorkoutExerciseDto();
        when(workoutService.updateWorkoutExercise(exerciseId, req)).thenReturn(dto);
        WorkoutExerciseDto result = controller.updateWorkoutExercise(workoutId, exerciseId, req);
        assertSame(dto, result);
    }

    @Test
    void deleteWorkoutExercise_callsService() {
        doNothing().when(workoutService).deleteWorkoutExercise(exerciseId);
        controller.deleteWorkoutExercise(workoutId, exerciseId);
        verify(workoutService).deleteWorkoutExercise(exerciseId);
    }
}


