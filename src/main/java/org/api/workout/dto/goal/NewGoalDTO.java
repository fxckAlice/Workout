package org.api.workout.dto.goal;

import java.time.LocalDateTime;

public record NewGoalDTO(
        String text,
        LocalDateTime deadline
) {
}
