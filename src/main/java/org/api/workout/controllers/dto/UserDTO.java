package org.api.workout.controllers.dto;


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
