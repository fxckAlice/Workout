package org.api.workout.controllers.dto.goal;

import java.time.LocalDateTime;

public record NewGoalDTO(
        String text,
        LocalDateTime deadline
) {
}
