package org.api.workout.controllers.dto.workout;

import org.api.workout.entities.workout.WorkoutType;

import java.time.LocalDateTime;

public record WorkoutDTO(
        long id,
        long authorId,
        LocalDateTime date,
        boolean isDone,
        WorkoutType type,
        Integer duration,
        LocalDateTime createdAt
) {
}
