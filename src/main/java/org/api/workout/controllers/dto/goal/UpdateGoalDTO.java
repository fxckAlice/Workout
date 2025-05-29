package org.api.workout.controllers.dto.goal;

import java.time.LocalDateTime;

public record UpdateGoalDTO(
        String text,
        boolean isDone,
        LocalDateTime deadline
) {
}
