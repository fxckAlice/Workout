package org.api.workout.dto.user;


import org.api.workout.dto.goal.GoalDTO;
import org.api.workout.dto.workout.WorkoutDTO;

import java.time.LocalDateTime;
import java.util.List;

public record UserDTO(
        Long id,
        String username,
        LocalDateTime createdAt,
        List<WorkoutDTO> workouts,
        List<GoalDTO> goals
) {
}
