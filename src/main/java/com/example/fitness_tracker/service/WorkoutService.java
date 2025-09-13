package com.example.fitness_tracker.service;

import com.example.fitness_tracker.domain.dto.Workout.CreateWorkoutDto;
import com.example.fitness_tracker.domain.dto.Workout.UpdateWorkoutDto;
import com.example.fitness_tracker.domain.dto.Workout.WorkoutDto;
import com.example.fitness_tracker.domain.dto.WorkoutExercise.CreateWorkoutExerciseDto;
import com.example.fitness_tracker.domain.dto.WorkoutExercise.WorkoutExerciseDto;
import com.example.fitness_tracker.domain.models.Exercise;
import com.example.fitness_tracker.domain.models.User;
import com.example.fitness_tracker.domain.models.Workout;
import com.example.fitness_tracker.domain.models.WorkoutExercise;
import com.example.fitness_tracker.mappers.WorkoutExerciseMapper;
import com.example.fitness_tracker.mappers.WorkoutMapper;
import com.example.fitness_tracker.repository.ExerciseRepository;
import com.example.fitness_tracker.repository.UserRepository;
import com.example.fitness_tracker.repository.WorkoutExerciseRepository;
import com.example.fitness_tracker.repository.WorkoutRepository;
import com.example.fitness_tracker.util.EntityNotFoundException;
import com.example.fitness_tracker.util.InvalidEntityDataException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final WorkoutMapper workoutMapper;
    private final WorkoutExerciseMapper wmMapper;
    /**
     * Retrieve all workouts that are not soft-deleted.
     *
     * @return List of {@link WorkoutDto} representing active workouts.
     */
    public List<WorkoutDto> getAll() {
        return workoutRepository.findAllByDeletedAtIsNull().stream()
                .map(workoutMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve a single workout by its ID if it is not soft-deleted.
     *
     * @param id Workout UUID.
     * @return The {@link WorkoutDto} representing the workout.
     * @throws EntityNotFoundException if the workout does not exist or is soft-deleted.
     */
    public WorkoutDto getById(UUID id) {
        Workout w = workoutRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Workout", id));
        return workoutMapper.toDto(w);
    }

    /**
     * Filter and sort workouts on the database side using JPA Specifications.
     * This avoids loading all records into memory.
     *
     * @param name   Optional partial name to filter by (case-insensitive).
     * @param shared Optional shared status to filter by.
     * @param sort   Sorting configuration (e.g. Sort.by("createdAt").descending()).
     * @return List of filtered & sorted {@link WorkoutDto}.
     */
    public List<WorkoutDto> getFilteredAndSortedWorkouts(String name,
                                                         Boolean shared,
                                                         Sort sort) {

        Specification<Workout> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isNull(root.get("deletedAt")));

            if (name != null && !name.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (shared != null) {
                predicates.add(cb.equal(root.get("isShared"), shared));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return workoutRepository.findAll(spec, sort).stream()
                .map(workoutMapper::toDto)
                .toList();
    }

    /**
     * Create a new workout with optional exercises.
     * Uses the authenticated user as the creator.
     * The operation is transactional to ensure atomicity.
     *
     * @param dto Data required to create a new workout.
     * @return Newly created {@link WorkoutDto}.
     * @throws InvalidEntityDataException if input data is invalid.
     * @throws RuntimeException if the authenticated user is missing.
     */
    @Transactional
    public WorkoutDto create(CreateWorkoutDto dto) {
        if (dto == null) {
            throw new InvalidEntityDataException("Workout data is required");
        }

        UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User createdBy = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new RuntimeException("User not logged in"));

        Workout workout = Workout.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .isShared(dto.isShared())
                .createdBy(createdBy)
                .build();

        if (dto.getWorkoutExercises() != null) {
            List<WorkoutExercise> exerciseLinks = dto.getWorkoutExercises().stream()
                    .map(exDto -> buildWorkoutExercise(workout, exDto))
                    .toList();
            workout.setWorkoutExercises(exerciseLinks);
        }

        return workoutMapper.toDto(workoutRepository.save(workout));
    }

    /**
     * Update a workout's basic details and exercises.
     * Exercises are updated in place: existing ones are updated if matching by order,
     * and new ones are added if they don't exist.
     * Transactional to ensure all updates succeed or none are applied.
     *
     * @param id  UUID of the workout to update.
     * @param dto Update payload.
     * @return Updated {@link WorkoutDto}.
     * @throws EntityNotFoundException if the workout does not exist.
     */
    @Transactional
    public WorkoutDto update(UUID id, UpdateWorkoutDto dto) {
        Workout existing = workoutRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Workout", id));

        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getIsShared() != null) existing.setShared(dto.getIsShared());

        if (dto.getWorkoutExercises() != null) {
            for (CreateWorkoutExerciseDto exDto : dto.getWorkoutExercises()) {
                Optional<WorkoutExercise> match = existing.getWorkoutExercises().stream()
                        .filter(we -> Objects.equals(we.getOrderInWorkout(), exDto.getOrderInWorkout()))
                        .findFirst();

                if (match.isPresent()) {
                    // Update in place
                    WorkoutExercise we = match.get();
                    updateWorkoutExerciseFields(we, exDto);
                } else {
                    // Add new exercise link
                    existing.getWorkoutExercises().add(buildWorkoutExercise(existing, exDto));
                }
            }
        }

        return workoutMapper.toDto(workoutRepository.save(existing));
    }

    /**
     * Add additional exercises to an existing workout without replacing existing ones.
     * Transactional to ensure atomicity.
     *
     * @param workoutId Workout UUID.
     * @param exercises List of exercises to add.
     * @return Updated {@link WorkoutDto} with new exercises added.
     * @throws EntityNotFoundException if the workout does not exist.
     */
    @Transactional
    public WorkoutDto addExercisesToWorkout(UUID workoutId, List<CreateWorkoutExerciseDto> exercises) {
        Workout existing = workoutRepository.findByIdAndDeletedAtIsNull(workoutId)
                .orElseThrow(() -> new EntityNotFoundException("Workout", workoutId));

        if (exercises != null && !exercises.isEmpty()) {

            // Find the current max order to start incrementing from it
            int currentMaxOrder = existing.getWorkoutExercises().stream()
                    .mapToInt(WorkoutExercise::getOrderInWorkout)
                    .max()
                    .orElse(0); // if no exercises, start at 0

            AtomicInteger nextOrder = new AtomicInteger(currentMaxOrder);

            List<WorkoutExercise> newLinks = exercises.stream()
                    .map(exDto -> buildWorkoutExercise(existing, exDto, nextOrder.incrementAndGet()))
                    .toList();


            existing.getWorkoutExercises().addAll(newLinks);
        }

        return workoutMapper.toDto(workoutRepository.save(existing));
    }

    /**
     * Soft delete a workout by setting its deletedAt timestamp.
     * Transactional to ensure the operation is atomic.
     *
     * @param id UUID of the workout to delete.
     * @throws EntityNotFoundException if the workout does not exist.
     */
    @Transactional
    public void delete(UUID id) {
        Workout existing = workoutRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Workout", id));
        existing.softDelete();
        workoutRepository.save(existing);
    }

    /**
     * Restore a previously soft-deleted workout.
     * Transactional to ensure the operation is atomic.
     *
     * @param id UUID of the workout to restore.
     * @throws EntityNotFoundException if the workout does not exist.
     */
    @Transactional
    public void restore(UUID id) {
        Workout existing = workoutRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Workout", id));
        existing.restore();
        workoutRepository.save(existing);
    }


    // Helper methods for update and create //
    /**
     * Helper method to construct a {@link WorkoutExercise} entity from its DTO.
     *
     * @param workout The parent workout.
     * @param dto     DTO containing exercise details.
     * @return A populated {@link WorkoutExercise}.
     * @throws EntityNotFoundException if the exercise name is not found.
     */
    private WorkoutExercise buildWorkoutExercise(Workout workout,
                                                 CreateWorkoutExerciseDto dto,
                                                 int order) {
        Exercise exercise = exerciseRepository.findByNameAndDeletedAtIsNull(dto.getExerciseName())
                .orElseThrow(() -> new EntityNotFoundException("Exercise", dto.getExerciseName()));

        return WorkoutExercise.builder()
                .workout(workout)
                .exercise(exercise)
                .sets(dto.getSets())
                .reps(dto.getReps())
                .duration(dto.getDuration())
                .calories(dto.getCalories())
                .orderInWorkout(order)
                .notes(dto.getNotes())
                .build();
    }

    private WorkoutExercise buildWorkoutExercise(Workout workout, CreateWorkoutExerciseDto dto) {
        return buildWorkoutExercise(
                workout,
                dto,
                dto.getOrderInWorkout() != null ? dto.getOrderInWorkout() : 0
        );
    }


    /**
     * Helper method to update fields of an existing {@link WorkoutExercise}.
     *
     * @param we   Existing WorkoutExercise entity.
     * @param dto  DTO containing new values.
     */
    private void updateWorkoutExerciseFields(WorkoutExercise we, CreateWorkoutExerciseDto dto) {
        if (dto.getSets() != null) we.setSets(dto.getSets());
        if (dto.getReps() != null) we.setReps(dto.getReps());
        if (dto.getDuration() != null) we.setDuration(dto.getDuration());
        if (dto.getCalories() != null) we.setCalories(dto.getCalories());
        if (dto.getNotes() != null) we.setNotes(dto.getNotes());
        if (dto.getOrderInWorkout() != null) we.setOrderInWorkout(dto.getOrderInWorkout());
        // Exercise name update is not allowed directly to preserve references
    }

    // WorkoutExercises Methods //

    /**
     * Retrieve all exercises of a given workout.
     *
     * @param workoutId UUID of the workout.
     * @return List of WorkoutExerciseDto.
     * @throws EntityNotFoundException if the workout does not exist.
     */
    public List<WorkoutExerciseDto> getWorkoutExercises(UUID workoutId) {
        Workout workout = workoutRepository.findByIdAndDeletedAtIsNull(workoutId)
                .orElseThrow(() -> new EntityNotFoundException("Workout", workoutId));

        return workout.getWorkoutExercises().stream()
                .map(wmMapper::toWorkoutExerciseDto)
                .sorted(Comparator.comparingInt(WorkoutExerciseDto::getOrderInWorkout))
                .toList();
    }

    /**
     * Retrieve a specific exercise inside a workout.
     *
     * @param workoutExerciseId UUID of the WorkoutExercise.
     * @return WorkoutExerciseDto.
     * @throws EntityNotFoundException if not found.
     */
    public WorkoutExerciseDto getWorkoutExercise(UUID workoutExerciseId) {
        WorkoutExercise we = workoutExerciseRepository.findByIdAndWorkoutDeletedAtIsNull(workoutExerciseId)
                .orElseThrow(() -> new EntityNotFoundException("WorkoutExercise", workoutExerciseId));
        return wmMapper.toWorkoutExerciseDto(we);
    }

    /**
     * Update a single WorkoutExercise.
     *
     * @param workoutExerciseId UUID of the WorkoutExercise.
     * @param dto DTO containing updated fields.
     * @return Updated WorkoutExerciseDto.
     * @throws EntityNotFoundException if not found.
     */
    @Transactional
    public WorkoutExerciseDto updateWorkoutExercise(UUID workoutExerciseId, CreateWorkoutExerciseDto dto) {
        WorkoutExercise existing = workoutExerciseRepository.findByIdAndWorkoutDeletedAtIsNull(workoutExerciseId)
                .orElseThrow(() -> new EntityNotFoundException("WorkoutExercise", workoutExerciseId));

        updateWorkoutExerciseFields(existing, dto);
        return wmMapper.toWorkoutExerciseDto(workoutExerciseRepository.save(existing));
    }

    /**
     * Delete a WorkoutExercise (hard delete from DB).
     * If you prefer soft-delete, add a deletedAt column and set it instead.
     *
     * @param workoutExerciseId UUID of the WorkoutExercise.
     * @throws EntityNotFoundException if not found.
     */
    @Transactional
    public void deleteWorkoutExercise(UUID workoutExerciseId) {
        WorkoutExercise existing = workoutExerciseRepository.findByIdAndWorkoutDeletedAtIsNull(workoutExerciseId)
                .orElseThrow(() -> new EntityNotFoundException("WorkoutExercise", workoutExerciseId));
        workoutExerciseRepository.delete(existing);
    }

}