package com.example.fitness_tracker;

import com.example.fitness_tracker.controller.OfflineEntryController;
import com.example.fitness_tracker.domain.dto.OfflineEntry.OfflineEntryRequestDto;
import com.example.fitness_tracker.domain.dto.OfflineEntry.OfflineEntryResponseDto;
import com.example.fitness_tracker.security.JwtService;
import com.example.fitness_tracker.service.OfflineEntryService;
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
class OfflineEntryControllerTest {

    @Mock
    private OfflineEntryService offlineEntryService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private OfflineEntryController offlineEntryController;

    private UUID userId;
    private UUID entryId;
    private OfflineEntryResponseDto responseDto;

    @BeforeEach
    void setup() {
        userId = UUID.randomUUID();
        entryId = UUID.randomUUID();
        responseDto = OfflineEntryResponseDto.builder()
                .entryId(entryId)
                .build();
        when(jwtService.extractUserId("token123")).thenReturn(userId);
    }

    @Test
    void getAllOfflineEntries_returnsList() {
        when(offlineEntryService.getAllOfflineEntries(userId)).thenReturn(List.of(responseDto));

        ResponseEntity<List<OfflineEntryResponseDto>> response = offlineEntryController.getAllOfflineEntries("Bearer token123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(jwtService).extractUserId("token123");
        verify(offlineEntryService).getAllOfflineEntries(userId);
    }

    @Test
    void getOfflineEntryById_returnsEntry() {
        when(offlineEntryService.getOfflineEntryById(entryId, userId)).thenReturn(responseDto);

        ResponseEntity<OfflineEntryResponseDto> response = offlineEntryController.getOfflineEntryById(entryId, "Bearer token123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(entryId, response.getBody().getEntryId());
    }

    @Test
    void createOfflineEntry_returnsCreated() {
        OfflineEntryRequestDto requestDto = new OfflineEntryRequestDto();
        when(offlineEntryService.createOfflineEntry(any(OfflineEntryRequestDto.class))).thenReturn(responseDto);

        ResponseEntity<OfflineEntryResponseDto> response = offlineEntryController.createOfflineEntry(requestDto, "Bearer token123");

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        assertEquals(userId, requestDto.getUserId());
    }

    @Test
    void updateOfflineEntry_returnsOk() {
        OfflineEntryRequestDto requestDto = new OfflineEntryRequestDto();
        when(offlineEntryService.updateOfflineEntry(eq(entryId), any(OfflineEntryRequestDto.class))).thenReturn(responseDto);

        ResponseEntity<OfflineEntryResponseDto> response = offlineEntryController.updateOfflineEntry(entryId, requestDto, "Bearer token123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        assertEquals(userId, requestDto.getUserId());
    }

    @Test
    void deleteOfflineEntry_returnsNoContent() {
        doNothing().when(offlineEntryService).deleteOfflineEntry(entryId, userId);

        ResponseEntity<Void> response = offlineEntryController.deleteOfflineEntry(entryId, "Bearer token123");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(offlineEntryService).deleteOfflineEntry(entryId, userId);
    }
}


