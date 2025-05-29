package org.api.workout.controllers.dto.stats;

import org.api.workout.controllers.dto.goal.GoalDTO;

public record StatsGoalDTO(
        long authorId,
        int totalGoals,
        int completedGoals,
        GoalDTO nearestGoal
) {
}
