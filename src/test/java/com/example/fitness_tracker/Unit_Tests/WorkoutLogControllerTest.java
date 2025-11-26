package com.example.fitness_tracker.Unit_Tests;

import com.example.fitness_tracker.controller.WorkoutLogController;
import com.example.fitness_tracker.domain.dto.WorkoutLog.WorkoutLogDto;
import com.example.fitness_tracker.domain.dto.common.ErrorResponse;
import com.example.fitness_tracker.service.WorkoutLogService;
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
class WorkoutLogControllerTest {

    @Mock private WorkoutLogService service;
    @InjectMocks private WorkoutLogController controller;

    private String auth;
    private UUID logId;
    private UUID userId;

    @BeforeEach
    void setup() {
        auth = "Bearer token";
        logId = UUID.randomUUID();
        userId = UUID.randomUUID();
    }

    @Test
    void createLog_returnsOkOrBadRequest() {
        WorkoutLogDto dto = new WorkoutLogDto();
        when(service.createLog(auth, dto, userId)).thenReturn(dto);
        ResponseEntity<?> ok = controller.createLog(auth, dto, userId);
        assertEquals(HttpStatus.OK, ok.getStatusCode());

        when(service.createLog(auth, dto, userId)).thenThrow(new RuntimeException("bad"));
        ResponseEntity<?> bad = controller.createLog(auth, dto, userId);
        assertEquals(HttpStatus.BAD_REQUEST, bad.getStatusCode());
        assertTrue(bad.getBody() instanceof ErrorResponse);
    }

    @Test
    void getLog_returnsOkOrNotFound() {
        WorkoutLogDto dto = new WorkoutLogDto();
        when(service.getLog(auth, logId, userId)).thenReturn(dto);
        ResponseEntity<?> ok = controller.getLog(auth, logId, userId);
        assertEquals(HttpStatus.OK, ok.getStatusCode());

        when(service.getLog(auth, logId, userId)).thenThrow(new RuntimeException("nf"));
        ResponseEntity<?> nf = controller.getLog(auth, logId, userId);
        assertEquals(HttpStatus.NOT_FOUND, nf.getStatusCode());
    }

    @Test
    void getAllForUser_returnsOk() {
        when(service.getAllForUser(auth, userId)).thenReturn(List.of());
        ResponseEntity<?> ok = controller.getAllForUser(auth, userId);
        assertEquals(HttpStatus.OK, ok.getStatusCode());
    }

    @Test
    void getAll_returnsOkOrUnauthorized() {
        when(service.getAll(auth)).thenReturn(List.of());
        ResponseEntity<?> ok = controller.getAll(auth);
        assertEquals(HttpStatus.OK, ok.getStatusCode());

        when(service.getAll(auth)).thenThrow(new RuntimeException("unauth"));
        ResponseEntity<?> unauth = controller.getAll(auth);
        assertEquals(HttpStatus.UNAUTHORIZED, unauth.getStatusCode());
    }

    @Test
    void updateLog_returnsOkOrBadRequest() {
        WorkoutLogDto dto = new WorkoutLogDto();
        when(service.updateLog(auth, logId, dto, userId)).thenReturn(dto);
        ResponseEntity<?> ok = controller.updateLog(auth, logId, dto, userId);
        assertEquals(HttpStatus.OK, ok.getStatusCode());

        when(service.updateLog(auth, logId, dto, userId)).thenThrow(new RuntimeException("bad"));
        ResponseEntity<?> bad = controller.updateLog(auth, logId, dto, userId);
        assertEquals(HttpStatus.BAD_REQUEST, bad.getStatusCode());
    }

    @Test
    void deleteLog_returnsOkOrNotFound() {
        doNothing().when(service).deleteLog(auth, logId, userId);
        ResponseEntity<?> ok = controller.deleteLog(auth, logId, userId);
        assertEquals(HttpStatus.OK, ok.getStatusCode());

        doThrow(new RuntimeException("nf")).when(service).deleteLog(auth, logId, userId);
        ResponseEntity<?> nf = controller.deleteLog(auth, logId, userId);
        assertEquals(HttpStatus.NOT_FOUND, nf.getStatusCode());
    }
}


