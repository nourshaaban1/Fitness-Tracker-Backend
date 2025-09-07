package com.example.fitness_tracker.service;

import com.example.fitness_tracker.domain.dto.Auth.LoginRequest;
import com.example.fitness_tracker.domain.dto.Auth.LoginResponse;
import com.example.fitness_tracker.domain.dto.Auth.SignupRequest;
import com.example.fitness_tracker.domain.enums.Role;
import com.example.fitness_tracker.domain.models.User;
import com.example.fitness_tracker.repository.UserRepository;
import com.example.fitness_tracker.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Signup
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

        MultipartFile profilePic = request.getProfilePic();
        String profilePicPath = null;
        if (profilePic != null && !profilePic.isEmpty()) {
            try {
                // Validate content type
                String contentType = profilePic.getContentType();
                if (contentType == null || 
                !(contentType.equalsIgnoreCase("image/jpeg") || 
                    contentType.equalsIgnoreCase("image/png") || 
                    contentType.equalsIgnoreCase("image/gif"))) {
                    throw new IllegalArgumentException("Invalid file type. Only JPEG, PNG, GIF are allowed.");
                }

                // Optional: validate file extension as extra safety
                String originalFilename = profilePic.getOriginalFilename();
                if (originalFilename != null &&
                    !(originalFilename.toLowerCase().endsWith(".jpg") ||
                    originalFilename.toLowerCase().endsWith(".jpeg") ||
                    originalFilename.toLowerCase().endsWith(".png") ||
                    originalFilename.toLowerCase().endsWith(".gif"))) {
                    throw new IllegalArgumentException("Invalid file extension. Only .jpg, .jpeg, .png, .gif allowed.");
                }

                // Save file
                String fileName = UUID.randomUUID() + "_" + profilePic.getOriginalFilename();
                
                Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads/profile-pics");
                Files.createDirectories(uploadPath);

                Path filePath = uploadPath.resolve(fileName);
                profilePic.transferTo(filePath.toFile());

                profilePicPath = "/uploads/profile-pics/" + fileName; // save relative path in DB
            } catch (IllegalArgumentException e) {
                throw e; // propagate validation exception
            } catch (Exception e) {
                throw new RuntimeException("Failed to save profile picture: " + e.getMessage());
            }
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .profilePic(profilePicPath)
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

    // Login
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        // update last login
        user.setLastLogin(Instant.now());
        user.setTokenVersion(user.getTokenVersion() + 1); // invalidate old tokens
        
        userRepository.save(user);

        String token = jwtService.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole().name(),
                user.getTokenVersion()
        );

        return new LoginResponse(token);
    }

    // Logout
    public void logout(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            UUID userId = jwtService.extractUserId(token);

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // increment token version -> all old tokens are invalidated
            user.setTokenVersion(user.getTokenVersion() + 1);

            userRepository.save(user);
        }
    }
}