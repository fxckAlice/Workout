package org.api.workout.dto.workout;

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
