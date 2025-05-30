package org.api.workout.dto.workout;

import org.api.workout.entities.workout.WorkoutType;

import java.time.LocalDateTime;

public record UpdateWorkoutDTO(
        LocalDateTime date,
        Boolean isDone,
        WorkoutType type,
        Integer duration
) {
}
