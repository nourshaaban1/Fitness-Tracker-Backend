package com.example.fitness_tracker.service;

import com.example.fitness_tracker.domain.dto.Profile.UpdateProfileRequest;
import com.example.fitness_tracker.domain.dto.Profile.UpdateProfileResponse;
import com.example.fitness_tracker.domain.dto.Profile.UserDto;
import com.example.fitness_tracker.domain.models.User;
import com.example.fitness_tracker.domain.models.UserPreferences;
import com.example.fitness_tracker.repository.UserRepository;
import com.example.fitness_tracker.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Admin only endpoint to get all users
    public List<UserDto> getAllUsers(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        String role = jwtService.extractRole(token);

        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new RuntimeException("Access denied: Admins only");
        }

        List<User> users = userRepository.findAllByDeletedAtIsNull();

        return users.stream()
                .map(user -> {
                    UserPreferences prefs = user.getPreferences();
                    return new UserDto(
                            user.getFirstName(),
                            user.getLastName(),
                            user.getEmail(),
                            user.getProfilePic(),
                            prefs != null ? prefs.getTheme() : null,
                            prefs != null ? prefs.isNotificationsEnabled() : null,
                            prefs != null ? prefs.getWorkoutReminderTime() : null
                    );
                })
                .toList();
    }

    // Get profile (own or any if admin)
    public UserDto getProfile(String authHeader, UUID targetUserId) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        UUID requesterId = jwtService.extractUserId(token);
        String role = jwtService.extractRole(token);

        User user;
        if ("ADMIN".equalsIgnoreCase(role) && targetUserId != null) {
            user = userRepository.findByIdAndDeletedAtIsNull(targetUserId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } else {
            user = userRepository.findByIdAndDeletedAtIsNull(requesterId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        // Map to DTO
        UserPreferences prefs = user.getPreferences();
        return new UserDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getProfilePic(),
                prefs != null ? prefs.getTheme() : null,
                prefs != null ? prefs.isNotificationsEnabled() : null,
                prefs != null ? prefs.getWorkoutReminderTime() : null
        );
    }

    // Update profile (own or any if admin)
    public UpdateProfileResponse updateProfile(String authHeader, UpdateProfileRequest request, UUID targetUserId) throws Exception {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        UUID requesterId = jwtService.extractUserId(token);
        String role = jwtService.extractRole(token);

        User user;
        if ("ADMIN".equalsIgnoreCase(role) && targetUserId != null) {
            user = userRepository.findByIdAndDeletedAtIsNull(targetUserId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } else {
            user = userRepository.findByIdAndDeletedAtIsNull(requesterId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        // Keep track if email changed
        boolean emailChanged = false;

        // Update user fields
        if (request.getFirstName() != null && !request.getFirstName().isBlank()) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null && !request.getLastName().isBlank()) user.setLastName(request.getLastName());

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmailAndDeletedAtIsNull(request.getEmail())) {
                throw new IllegalArgumentException("Email already exists!");
            }
            user.setEmail(request.getEmail());
            user.setTokenVersion(user.getTokenVersion() + 1); // invalidate old tokens
            emailChanged = true;
        }

        if (request.getPassword() != null) {
            if (request.getPassword().length() < 6) {
                throw new IllegalArgumentException("Password must be at least 6 characters");
            }
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        // Handle profile picture
        MultipartFile profilePic = request.getProfilePic();
        if (profilePic != null) {
            if (profilePic.isEmpty()) {
                // User wants to delete the profile picture
                user.setProfilePic(null);
            } else {
                // User uploaded a new picture
                if (profilePic.getContentType() == null || !profilePic.getContentType().startsWith("image/")) {
                    throw new IllegalArgumentException("Profile picture must be an image");
                }

                String fileName = UUID.randomUUID() + "_" + profilePic.getOriginalFilename();
                Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads/profile-pics");
                Files.createDirectories(uploadPath);
                Path filePath = uploadPath.resolve(fileName);
                profilePic.transferTo(filePath.toFile());
                user.setProfilePic("/uploads/profile-pics/" + fileName);
            }
        }

        // --- Update or create preferences ---
        UserPreferences preferences = user.getPreferences();
        if (preferences == null) {
            preferences = new UserPreferences();
            preferences.setUser(user);
            user.setPreferences(preferences);
        }

        if (request.getTheme() != null) {
            preferences.setTheme(request.getTheme()); // request.getTheme() must be of type Theme
        }
        if (request.getNotificationsEnabled() != null) {
            preferences.setNotificationsEnabled(request.getNotificationsEnabled());
        }
        preferences.setWorkoutReminderTime(request.getWorkoutReminderTime());

        // Save user and cascade save preferences
        User updated = userRepository.save(user);

        // Regenerate new token only if email changed
        String newToken = null;
        if (emailChanged) {
            newToken = jwtService.generateToken(
                    updated.getId(),
                    updated.getEmail(),
                    updated.getRole().name(),
                    updated.getTokenVersion()
            );
        }

        // --- Map to DTO ---
        UserDto dto = new UserDto(
                updated.getFirstName(),
                updated.getLastName(),
                updated.getEmail(),
                updated.getProfilePic(),
                updated.getPreferences().getTheme(),
                updated.getPreferences().isNotificationsEnabled(),
                updated.getPreferences().getWorkoutReminderTime()
        );

        return new UpdateProfileResponse(dto, newToken);
    }

    // Delete profile (soft delete) (own or any if admin)
    public void deleteProfile(String authHeader, UUID targetUserId) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        UUID requesterId = jwtService.extractUserId(token);
        String role = jwtService.extractRole(token);

        User user;
        if ("ADMIN".equalsIgnoreCase(role) && targetUserId != null) {
            user = userRepository.findByIdAndDeletedAtIsNull(targetUserId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } else {
            user = userRepository.findByIdAndDeletedAtIsNull(requesterId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        // Soft delete using your BaseEntityWithSoftDelete method
        user.softDelete();

        // Make email reusable
        user.setEmail(user.getEmail() + "_deleted_" + System.currentTimeMillis());

        // Invalidate all tokens immediately
        user.setTokenVersion(user.getTokenVersion() + 1);

        userRepository.save(user);
    }
}