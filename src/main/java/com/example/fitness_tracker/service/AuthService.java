package com.example.fitness_tracker.service;

import com.example.fitness_tracker.domain.dto.Auth.LoginResponse;
import com.example.fitness_tracker.domain.dto.Auth.SignupRequest;
import com.example.fitness_tracker.domain.enums.Role;
import com.example.fitness_tracker.domain.models.User;
import com.example.fitness_tracker.repository.UserRepository;
import com.example.fitness_tracker.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse signup(SignupRequest request, String authHeader) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists!");
        }

        // Only allow ADMIN creation by existing ADMIN
        Role role = request.getRole() != null ? request.getRole() : Role.USER;
        if (role == Role.ADMIN) {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Unauthorized: Admin token required to create another Admin");
            }
            String token = authHeader.substring(7);
            String tokenRole = jwtService.extractRole(token);
            if (!"ADMIN".equalsIgnoreCase(tokenRole)) {
                throw new RuntimeException("Unauthorized: Only Admin can create another Admin");
            }
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .profilePic(request.getProfilePic())
                .role(role)
                .lastLogin(Instant.now())
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole().name(),
                user.getTokenVersion()
        );

        return new LoginResponse(token); // directly return login response
    }
}