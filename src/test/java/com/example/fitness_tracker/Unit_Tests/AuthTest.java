package com.example.fitness_tracker.Unit_Tests;

import com.example.fitness_tracker.controller.AuthController;
import com.example.fitness_tracker.domain.dto.Auth.LoginRequest;
import com.example.fitness_tracker.domain.dto.Auth.LoginResponse;
import com.example.fitness_tracker.domain.dto.Auth.SignupRequest;
import com.example.fitness_tracker.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private SignupRequest signupRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setup() {
        signupRequest = new SignupRequest();
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setEmail("john@example.com");
        signupRequest.setPassword("secret123");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("john@example.com");
        loginRequest.setPassword("secret123");
    }

    @Test
    void signup_success() {
        when(authService.signup(any(SignupRequest.class), any()))
                .thenReturn(new LoginResponse("token123"));

        ResponseEntity<?> response = authController.signup(signupRequest, null);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof LoginResponse);
        assertEquals("token123", ((LoginResponse) response.getBody()).getToken());
    }

    @Test
    void signup_badRequest() {
        when(authService.signup(any(SignupRequest.class), any()))
                .thenThrow(new IllegalArgumentException("Invalid data"));

        ResponseEntity<?> response = authController.signup(signupRequest, null);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void login_success() {
        when(authService.login(any(LoginRequest.class)))
                .thenReturn(new LoginResponse("token123"));

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("token123", ((LoginResponse) response.getBody()).getToken());
    }

    @Test
    void logout_success() {
        doNothing().when(authService).logout("Bearer token123");

        ResponseEntity<?> response = authController.logout("Bearer token123");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Logged out successfully", response.getBody());
    }
}
