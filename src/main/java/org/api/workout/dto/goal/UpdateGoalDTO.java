package org.api.workout.dto.goal;

import java.time.LocalDateTime;

public record UpdateGoalDTO(
        String text,
        Boolean isDone,
        LocalDateTime deadline
) {
}
