package org.api.workout.controllers.dto.workout;

import org.api.workout.entities.workout.WorkoutType;

import java.time.LocalDateTime;

public record NewWorkoutDTO(
        LocalDateTime date,
        WorkoutType type,
        Integer duration
) {
}
