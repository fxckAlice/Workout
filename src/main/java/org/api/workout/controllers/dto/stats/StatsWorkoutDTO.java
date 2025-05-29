package org.api.workout.controllers.dto.stats;

import org.api.workout.controllers.dto.workout.WorkoutDTO;

public record StatsWorkoutDTO(
        long authorId,
        int totalWorkouts,
        int completedWorkouts,
        StatsByTypeDTO allByType,
        StatsByTypeDTO completedByType,
        WorkoutDTO nextWorkout,
        WorkoutDTO lastWorkout
) {
}
