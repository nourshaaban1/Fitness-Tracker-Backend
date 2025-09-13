package com.example.fitness_tracker.service;

import com.example.fitness_tracker.domain.dto.Auth.LoginRequest;
import com.example.fitness_tracker.domain.dto.Auth.LoginResponse;
import com.example.fitness_tracker.domain.dto.Auth.OtpRequest;
import com.example.fitness_tracker.domain.dto.Auth.ResetPasswordRequest;
import com.example.fitness_tracker.domain.dto.Auth.SignupRequest;
import com.example.fitness_tracker.domain.enums.Role;
import com.example.fitness_tracker.domain.models.Otp;
import com.example.fitness_tracker.domain.models.User;
import com.example.fitness_tracker.domain.models.UserPreferences;
import com.example.fitness_tracker.repository.OtpRepository;
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
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtService jwtService;

    // Signup
    public LoginResponse signup(SignupRequest request, String authHeader) {
        if (userRepository.existsByEmailAndDeletedAtIsNull(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists!");
        }

        // Only allow ADMIN creation by existing ADMIN
        Role role = request.getRole() != null ? request.getRole() : Role.USER;
        // if (role == Role.ADMIN) {
        //     if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        //         throw new RuntimeException("Unauthorized: Admin token required to create another Admin");
        //     }
        //     String token = authHeader.substring(7);
        //     String tokenRole = jwtService.extractRole(token);
        //     if (!"ADMIN".equalsIgnoreCase(tokenRole)) {
        //         throw new RuntimeException("Unauthorized: Only Admin can create another Admin");
        //     }
        // }

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

        // Create default preferences
        UserPreferences preferences = new UserPreferences();
        preferences.setUser(user);
        // Default values are automatically set in UserPreferences (Theme.LIGHT, notificationsEnabled = true)
        user.setPreferences(preferences);

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
        User user = userRepository.findByEmailAndDeletedAtIsNull(request.getEmail())
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

    public void otpRequest(OtpRequest request) {
        String email = request.getEmail();

        userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new RuntimeException("No account found with this email"));

        // generate 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));

        Otp token = otpRepository.findByEmail(email).orElse(null);

        if (token != null) {
            token.setOtp(otp);
            token.setExpiry(LocalDateTime.now().plusMinutes(5));
            token.setUsed(false);
        } else {
            token = Otp.builder()
                    .email(email)
                    .otp(otp)
                    .expiry(LocalDateTime.now().plusMinutes(5))
                    .used(false)
                    .build();
        }

        otpRepository.save(token);

        // Send OTP via email
        // emailService.sendOtp(email, otp);
        System.out.println("OTP for " + email + ": " + otp); // for testing
    }

    public void resetPassword(ResetPasswordRequest request) {
        String email = request.getEmail();
        String otp = request.getOtp();

        Otp token = otpRepository.findByEmailAndOtp(email, otp)
                .orElseThrow(() -> new RuntimeException("Invalid OTP"));

        if (token.isUsed()) {
            throw new RuntimeException("OTP already used");
        }

        if (token.getExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        token.setUsed(true);
        otpRepository.save(token);

        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new RuntimeException("Account not found for email: " + email));

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    // Logout
    public void logout(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            UUID userId = jwtService.extractUserId(token);

            User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // increment token version -> all old tokens are invalidated
            user.setTokenVersion(user.getTokenVersion() + 1);

            userRepository.save(user);
        }
    }
}