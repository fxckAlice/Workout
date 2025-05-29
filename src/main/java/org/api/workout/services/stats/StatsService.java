package org.api.workout.services.stats;

import org.api.workout.dto.goal.GoalDTO;
import org.api.workout.dto.stats.StatsByTypeDTO;
import org.api.workout.dto.stats.StatsGoalDTO;
import org.api.workout.dto.stats.StatsWorkoutDTO;
import org.api.workout.dto.workout.WorkoutDTO;
import org.api.workout.entities.goals.Goal;
import org.api.workout.entities.workout.Workout;
import org.api.workout.entities.workout.WorkoutType;
import org.api.workout.services.goal.GoalService;
import org.api.workout.services.workout.WorkoutService;
import org.springframework.stereotype.Service;


@Service
public class StatsService {
    private final WorkoutService workoutService;
    private final GoalService goalService;

    public StatsService(WorkoutService workoutService, GoalService goalService) {
        this.workoutService = workoutService;
        this.goalService = goalService;
    }

    public StatsWorkoutDTO getWorkoutsStats(long userId) {
        int totalWorkouts = workoutService.countWorkouts(userId);
        int totalCompleted = workoutService.countWorkouts(userId, true);
        StatsByTypeDTO statsByTypeDTO = new StatsByTypeDTO(
                workoutService.countWorkouts(userId, WorkoutType.STRENGTH),
                workoutService.countWorkouts(userId, WorkoutType.CARDIO),
                workoutService.countWorkouts(userId, WorkoutType.GYMNASTICS),
                workoutService.countWorkouts(userId, WorkoutType.TRX),
                workoutService.countWorkouts(userId, WorkoutType.YOGA),
                workoutService.countWorkouts(userId, WorkoutType.OTHER)
        );
        StatsByTypeDTO completedByTypeDTO = new StatsByTypeDTO(
                workoutService.countWorkouts(userId, WorkoutType.STRENGTH, true),
                workoutService.countWorkouts(userId, WorkoutType.CARDIO, true),
                workoutService.countWorkouts(userId, WorkoutType.GYMNASTICS, true),
                workoutService.countWorkouts(userId, WorkoutType.TRX, true),
                workoutService.countWorkouts(userId, WorkoutType.YOGA, true),
                workoutService.countWorkouts(userId, WorkoutType.OTHER, true)
        );
        Workout lastCompleted = workoutService.getLastCompletedWorkout(userId);
        Workout nearestWorkout = workoutService.getFirstUncompletedWorkout(userId);
        WorkoutDTO lastCompletedWorkoutDTO = lastCompleted == null ? null : new WorkoutDTO(
                lastCompleted.getId(),
                lastCompleted.getAuthor().getId(),
                lastCompleted.getDate(),
                lastCompleted.isDone(),
                lastCompleted.getType(),
                lastCompleted.getDuration(),
                lastCompleted.getCreatedAt()
        );
        WorkoutDTO nearestWorkoutWorkoutDTO = nearestWorkout == null ? null : new WorkoutDTO(
                nearestWorkout.getId(),
                nearestWorkout.getAuthor().getId(),
                nearestWorkout.getDate(),
                nearestWorkout.isDone(),
                nearestWorkout.getType(),
                nearestWorkout.getDuration(),
                nearestWorkout.getCreatedAt()
        );
        return new StatsWorkoutDTO(
                userId,
                totalWorkouts,
                totalCompleted,
                statsByTypeDTO,
                completedByTypeDTO,
                lastCompletedWorkoutDTO,
                nearestWorkoutWorkoutDTO
        );
    }
    public StatsGoalDTO getGoalsStats(long userId) {
        int totalGoals = goalService.countGoals(userId);
        int totalCompleted = goalService.countGoals(userId, true);
        Goal nearestDeadlineGoal = goalService.getNearestDeadlineGoal(userId);
        GoalDTO nearestDeadlineGoalDTO = nearestDeadlineGoal == null ? null : new GoalDTO(
                nearestDeadlineGoal.getId(),
                nearestDeadlineGoal.getAuthor().getId(),
                nearestDeadlineGoal.getText(),
                nearestDeadlineGoal.isDone(),
                nearestDeadlineGoal.getDeadline(),
                nearestDeadlineGoal.getCreatedAt()
        );
        return new StatsGoalDTO(
                userId,
                totalGoals,
                totalCompleted,
                nearestDeadlineGoalDTO
        );
    }
}
