package org.api.workout.dto.goal;

import java.time.LocalDateTime;

public record GoalsFilterDTO(
        String authorId,
        String isDone,
        LocalDateTime deadlineDateFrom,
        LocalDateTime deadlineDateTo,
        LocalDateTime createdAtFrom,
        LocalDateTime createdAtTo
) {
}
