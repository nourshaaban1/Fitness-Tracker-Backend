package com.example.fitness_tracker;

import com.example.fitness_tracker.controller.ProfileController;
import com.example.fitness_tracker.domain.dto.Profile.UpdateProfileRequest;
import com.example.fitness_tracker.domain.dto.Profile.UpdateProfileResponse;
import com.example.fitness_tracker.domain.dto.Profile.UserDto;
import com.example.fitness_tracker.service.ProfileService;
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
class ProfileControllerTest {

    @Mock
    private ProfileService profileService;

    @InjectMocks
    private ProfileController profileController;

    private String authHeader;
    private UUID userId;

    @BeforeEach
    void setup() {
        authHeader = "Bearer token";
        userId = UUID.randomUUID();
    }

    @Test
    void getAllUsers_admin_returnsList() {
        when(profileService.getAllUsers(authHeader)).thenReturn(List.of(new UserDto(null, null, null, null, null, null, null)));

        ResponseEntity<?> response = profileController.getAllUsers(authHeader);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getProfile_returnsUserDto() {
        UserDto dto = new UserDto(null, null, null, null, null, null, null);
        when(profileService.getProfile(authHeader, userId)).thenReturn(dto);

        ResponseEntity<?> response = profileController.getProfile(authHeader, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(dto, response.getBody());
    }

    @Test
    void updateProfile_returnsResponseDto() throws Exception {
        UpdateProfileRequest request = new UpdateProfileRequest();
        UserDto user = new UserDto(null, null, null, null, null, null, null);
        UpdateProfileResponse responseDto = new UpdateProfileResponse(user, "newToken");
        when(profileService.updateProfile(authHeader, request, userId)).thenReturn(responseDto);

        ResponseEntity<?> response = profileController.updateProfile(authHeader, userId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(responseDto, response.getBody());
    }

    @Test
    void deleteProfile_returnsOkMessage() {
        doNothing().when(profileService).deleteProfile(authHeader, userId);

        ResponseEntity<?> response = profileController.deleteProfile(authHeader, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Profile deleted successfully", response.getBody());
    }
}


