package org.api.workout.controllers.dto.goal;

import java.time.LocalDateTime;

public record GoalDTO(
        long id,
        long authorId,
        String text,
        boolean isDone,
        LocalDateTime deadline,
        LocalDateTime createdAt
) {
}
