package com.example.fitness_tracker.repository;

import com.example.fitness_tracker.domain.models.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByEmail(String email);
    Optional<Otp> findByEmailAndOtp(String email, String otp);
}
