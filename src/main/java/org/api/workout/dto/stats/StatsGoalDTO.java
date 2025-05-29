package org.api.workout.dto.stats;

import org.api.workout.dto.goal.GoalDTO;

public record StatsGoalDTO(
        long authorId,
        int totalGoals,
        int completedGoals,
        GoalDTO nearestGoal
) {
}
