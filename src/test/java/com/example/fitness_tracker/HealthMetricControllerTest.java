package com.example.fitness_tracker;

import com.example.fitness_tracker.controller.HealthMetricController;
import com.example.fitness_tracker.domain.dto.HealthMetric.HealthMetricDto;
import com.example.fitness_tracker.domain.dto.common.ErrorResponse;
import com.example.fitness_tracker.service.HealthMetricService;
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
class HealthMetricControllerTest {

    @Mock private HealthMetricService service;
    @InjectMocks private HealthMetricController controller;

    private String auth;
    private UUID metricId;
    private UUID userId;

    @BeforeEach
    void setup() {
        auth = "Bearer token";
        metricId = UUID.randomUUID();
        userId = UUID.randomUUID();
    }

    @Test
    void createMetric_returnsOkOrBadRequest() {
        HealthMetricDto dto = HealthMetricDto.builder().weight(70.0f).build();
        when(service.createMetric(auth, dto, userId)).thenReturn(dto);

        ResponseEntity<?> ok = controller.createMetric(auth, dto, userId);
        assertEquals(HttpStatus.OK, ok.getStatusCode());

        when(service.createMetric(auth, dto, userId)).thenThrow(new RuntimeException("bad"));
        ResponseEntity<?> bad = controller.createMetric(auth, dto, userId);
        assertEquals(HttpStatus.BAD_REQUEST, bad.getStatusCode());
        assertTrue(bad.getBody() instanceof ErrorResponse);
    }

    @Test
    void getMetric_returnsOkOrNotFound() {
        HealthMetricDto dto = new HealthMetricDto();
        when(service.getMetric(auth, metricId, userId)).thenReturn(dto);
        ResponseEntity<?> ok = controller.getMetric(auth, metricId, userId);
        assertEquals(HttpStatus.OK, ok.getStatusCode());

        when(service.getMetric(auth, metricId, userId)).thenThrow(new RuntimeException("not found"));
        ResponseEntity<?> nf = controller.getMetric(auth, metricId, userId);
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
    void updateMetric_returnsOkOrBadRequest() {
        HealthMetricDto dto = new HealthMetricDto();
        when(service.updateMetric(auth, metricId, dto, userId)).thenReturn(dto);
        ResponseEntity<?> ok = controller.updateMetric(auth, metricId, dto, userId);
        assertEquals(HttpStatus.OK, ok.getStatusCode());

        when(service.updateMetric(auth, metricId, dto, userId)).thenThrow(new RuntimeException("bad"));
        ResponseEntity<?> bad = controller.updateMetric(auth, metricId, dto, userId);
        assertEquals(HttpStatus.BAD_REQUEST, bad.getStatusCode());
    }

    @Test
    void deleteMetric_returnsOkOrNotFound() {
        doNothing().when(service).deleteMetric(auth, metricId, userId);
        ResponseEntity<?> ok = controller.deleteMetric(auth, metricId, userId);
        assertEquals(HttpStatus.OK, ok.getStatusCode());

        doThrow(new RuntimeException("nf")).when(service).deleteMetric(auth, metricId, userId);
        ResponseEntity<?> nf = controller.deleteMetric(auth, metricId, userId);
        assertEquals(HttpStatus.NOT_FOUND, nf.getStatusCode());
    }
}


