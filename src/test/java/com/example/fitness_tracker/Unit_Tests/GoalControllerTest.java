package com.example.fitness_tracker.Unit_Tests;

import com.example.fitness_tracker.controller.GoalController;
import com.example.fitness_tracker.domain.dto.Goal.CreateGoalDto;
import com.example.fitness_tracker.domain.dto.Goal.GoalDto;
import com.example.fitness_tracker.domain.dto.Goal.UpdateGoalDto;
import com.example.fitness_tracker.domain.dto.common.ErrorResponse;
import com.example.fitness_tracker.service.GoalService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalControllerTest {

    @Mock private GoalService goalService;
    @InjectMocks private GoalController controller;

    private UUID goalId;
    private GoalDto goalDto;

    @BeforeEach
    void setup() {
        goalId = UUID.randomUUID();
        goalDto = GoalDto.builder().id(goalId).description("Lose weight").build();
    }

    @Test
    void getAllGoalsForCurrentUser_returnsList() {
        when(goalService.getAllGoalsForCurrentUser()).thenReturn(List.of(goalDto));

        ResponseEntity<List<GoalDto>> response = controller.getAllGoalsForCurrentUser();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getGoalByIdForCurrentUser_returnsGoal() {
        when(goalService.getGoalByIdForCurrentUser(goalId)).thenReturn(goalDto);

        ResponseEntity<GoalDto> response = controller.getGoalByIdForCurrentUser(goalId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(goalId, response.getBody().getId());
    }

    @Test
    void createGoal_returnsCreated() {
        CreateGoalDto createDto = CreateGoalDto.builder().description("Lose weight").build();
        when(goalService.createGoal(createDto)).thenReturn(goalDto);

        ResponseEntity<GoalDto> response = controller.createGoal(createDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(goalDto, response.getBody());
    }

    @Test
    void updateGoal_returnsOk() {
        UpdateGoalDto updateDto = UpdateGoalDto.builder().description("Update").build();
        when(goalService.updateGoal(goalId, updateDto)).thenReturn(goalDto);

        ResponseEntity<GoalDto> response = controller.updateGoal(goalId, updateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(goalDto, response.getBody());
    }

    @Test
    void deleteGoal_returnsMessage() {
        doNothing().when(goalService).delete(goalId);

        ResponseEntity<ErrorResponse> response = controller.deleteGoal(goalId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted Successfully", response.getBody().getMessage());
    }

    @Test
    void restoreGoal_returnsMessage() {
        doNothing().when(goalService).restore(goalId);

        ResponseEntity<ErrorResponse> response = controller.restoreGoal(goalId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Restored Successfully", response.getBody().getMessage());
    }
}


