package com.example.fitness_tracker.mappers;

import com.example.fitness_tracker.domain.dto.WorkoutExercise.ExercisesInWorkouts;
import com.example.fitness_tracker.domain.models.WorkoutExercise;
import com.example.fitness_tracker.repository.ExerciseRepository;
import com.example.fitness_tracker.util.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class WorkoutExerciseMapper {

    private final ExerciseRepository exerciseRepository;

//    public WorkoutExerciseDto toDto(WorkoutExercise we) {
//        if (we == null) return null;
//
//        return WorkoutExerciseDto.builder()
//                .id(we.getId())
//                .exerciseId(we.getExercise().getId())
//                .sets(we.getSets())
//                .reps(we.getReps())
//                .duration(we.getDuration())
//                .calories(we.getCalories())
//                .orderInWorkout(we.getOrderInWorkout())
//                .notes(we.getNotes())
//                .build();
//    }

    public ExercisesInWorkouts toExerciseInWorkout(WorkoutExercise we) {
        if (we == null) return null;

        String exerciseName = exerciseRepository
                .findByIdAndDeletedAtIsNull(we.getExercise().getId())
                .orElseThrow(() -> new EntityNotFoundException("exercise", we.getExercise().getId()))
                .getName();

        return ExercisesInWorkouts.builder()
                .exerciseName(exerciseName)
                .sets(we.getSets())
                .reps(we.getReps())
                .duration(we.getDuration())
                .calories(we.getCalories())
                .orderInWorkout(we.getOrderInWorkout())
                .notes(we.getNotes())
                .build();
    }

    // updateEntityFromDto(...) and toEntity(...) remain the same
}
