package org.api.workout.controllers.dto;

import org.api.workout.entities.workout.WorkoutType;

import java.time.LocalDateTime;

public record WorkoutFilterDTO(
        String authorId,
        String isDone,
        LocalDateTime dateFrom,
        LocalDateTime dateTo,
        WorkoutType type
) {
}
