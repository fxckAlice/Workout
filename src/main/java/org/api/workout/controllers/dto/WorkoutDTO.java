package org.api.workout.controllers.dto;

import org.api.workout.enteties.workout.WorkoutType;

import java.time.LocalDateTime;

public record WorkoutDTO(
        long id,
        long authorId,
        LocalDateTime date,
        boolean isDone,
        WorkoutType type,
        int duration,
        LocalDateTime createdAt
) {
}
