package com.example.fitness_tracker.service;

import com.example.fitness_tracker.domain.dto.Exercise.UpdateExerciseDto;
import com.example.fitness_tracker.repository.UserRepository;
import com.example.fitness_tracker.util.InvalidEntityDataException;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    @Transactional
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
        return exerciseRepository.findAllByDeletedAtIsNull()
                .stream()
                .map(ExerciseMapper::toDto)
                .toList();
    }

    /**
     * Filter and sort exercises on the database side using JPA Specifications.
     * This prevents loading all records into memory.
     *
     * @param name        Optional name filter (case-insensitive).
     * @param category    Optional category filter (case-insensitive).
     * @param sort        Sorting configuration (e.g. Sort.by("name").ascending()).
     * @return List of filtered and sorted {@link ExerciseDto}.
     */
    public List<ExerciseDto> getFilteredAndSortedExercises(
            String name,
            String category,
            Sort sort
    ) {
        Specification<Exercise> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isNull(root.get("deletedAt"))); // exclude soft-deleted

            if (name != null && !name.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (category != null && !category.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("muscleGroup")), "%" + category.toLowerCase() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return exerciseRepository.findAll(spec, sort == null ? Sort.unsorted() : sort)
                .stream()
                .map(ExerciseMapper::toDto)
                .toList();
    }


    @Transactional
    public ExerciseDto update(UUID id, UpdateExerciseDto dto) {
        Exercise existing = exerciseRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Exercise", id));
        if (dto == null) {
            throw new InvalidEntityDataException("Update data cannot be null");
        }

        ExerciseMapper.updateEntityFromDto(existing, dto);
        return ExerciseMapper.toDto(exerciseRepository.save(existing));
    }

    @Transactional
    public void delete(UUID id) {
        Exercise existing = exerciseRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Exercise", id));
        existing.softDelete();
        exerciseRepository.save(existing);
    }

    @Transactional
    public void restore(UUID id) {
        Exercise existing = exerciseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Exercise", id));
        existing.restore();
        exerciseRepository.save(existing);
    }
}
