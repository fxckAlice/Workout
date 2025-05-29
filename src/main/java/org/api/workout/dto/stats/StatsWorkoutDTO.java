package org.api.workout.dto.stats;

import org.api.workout.dto.workout.WorkoutDTO;

public record StatsWorkoutDTO(
        long authorId,
        int totalWorkouts,
        int completedWorkouts,
        StatsByTypeDTO allByType,
        StatsByTypeDTO completedByType,
        WorkoutDTO lastWorkout,
        WorkoutDTO nextWorkout
) {
}
