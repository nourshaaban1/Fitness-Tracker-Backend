package com.example.fitness_tracker.service;

import com.example.fitness_tracker.domain.dto.Exercise.UpdateExerciseDto;
import com.example.fitness_tracker.repository.UserRepository;
import com.example.fitness_tracker.util.InvalidEntityDataException;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.fitness_tracker.domain.dto.Exercise.CreateExerciseDto;
import com.example.fitness_tracker.domain.dto.Exercise.ExerciseDto;
import com.example.fitness_tracker.domain.models.Exercise;
import com.example.fitness_tracker.domain.models.User;
import com.example.fitness_tracker.mappers.ExerciseMapper;
import com.example.fitness_tracker.repository.ExerciseRepository;
import com.example.fitness_tracker.util.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    public ExerciseDto create(CreateExerciseDto dto) {
        UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new RuntimeException("User not logged in"));

        Exercise exercise = Exercise.builder()
                .createdBy(user)
                .imageUrl(dto.getImageUrl())
                .hasWeights(dto.isHasWeights())
                .trackingMode(dto.getTrackingMode())
                .description(dto.getDescription())
                .name(dto.getName())
                .category(dto.getCategory())
                .build();
        return ExerciseMapper.toDto(exerciseRepository.save(exercise));
    }

    public ExerciseDto getById(UUID id) {
        return exerciseRepository.findByIdAndDeletedAtIsNull(id)
                .map(ExerciseMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Exercise", id));
    }

    public List<ExerciseDto> getAll() {
        return exerciseRepository.findAllByDeletedAtIsNull().stream()
                .map(ExerciseMapper::toDto)
                .collect(Collectors.toList());
    }

    public ExerciseDto update(UUID id, UpdateExerciseDto dto) {
        Exercise existing = exerciseRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Exercise", id));
        if (dto == null) {
            throw new InvalidEntityDataException("Update data cannot be null");
        }

        ExerciseMapper.updateEntityFromDto(existing, dto);
        return ExerciseMapper.toDto(exerciseRepository.save(existing));
    }

    public void delete(UUID id) {
        Exercise existing = exerciseRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Exercise", id));
        existing.softDelete();
        exerciseRepository.save(existing);
    }

    public void restore(UUID id) {
        Exercise existing = exerciseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Exercise", id));
        existing.restore();
        exerciseRepository.save(existing);
    }
}
