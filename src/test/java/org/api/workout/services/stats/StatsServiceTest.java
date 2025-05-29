package org.api.workout.services.stats;

import org.api.workout.dto.stats.StatsGoalDTO;
import org.api.workout.dto.stats.StatsWorkoutDTO;
import org.api.workout.entities.goals.Goal;
import org.api.workout.entities.user.User;
import org.api.workout.entities.workout.Workout;
import org.api.workout.entities.workout.WorkoutType;
import org.api.workout.services.goal.GoalService;
import org.api.workout.services.workout.WorkoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatsServiceTest {

    private WorkoutService workoutService;
    private GoalService goalService;
    private StatsService statsService;

    @BeforeEach
    void setUp() {
        workoutService = mock(WorkoutService.class);
        goalService = mock(GoalService.class);
        statsService = new StatsService(workoutService, goalService);
    }

    @Test
    void getWorkoutsStats_ShouldReturnCorrectStats() {
        long userId = 1L;

        when(workoutService.countWorkouts(userId)).thenReturn(10);
        when(workoutService.countWorkouts(userId, true)).thenReturn(7);

        for (WorkoutType type : WorkoutType.values()) {
            when(workoutService.countWorkouts(userId, type)).thenReturn(1);
            when(workoutService.countWorkouts(userId, type, true)).thenReturn(1);
        }

        User user = new User(); user.setId(userId);

        Workout last = new Workout();
        last.setId(1L);
        last.setAuthor(user);
        last.setDate(LocalDateTime.now().minusDays(1));
        last.setDone(true);
        last.setType(WorkoutType.CARDIO);
        last.setDuration(30);
        last.setCreatedAt(LocalDateTime.now().minusDays(2));

        Workout nearest = new Workout();
        nearest.setId(2L);
        nearest.setAuthor(user);
        nearest.setDate(LocalDateTime.now().plusDays(1));
        nearest.setDone(false);
        nearest.setType(WorkoutType.YOGA);
        nearest.setDuration(45);
        nearest.setCreatedAt(LocalDateTime.now());

        when(workoutService.getLastCompletedWorkout(userId)).thenReturn(last);
        when(workoutService.getFirstUncompletedWorkout(userId)).thenReturn(nearest);

        StatsWorkoutDTO result = statsService.getWorkoutsStats(userId);

        assertEquals(10, result.totalWorkouts());
        assertEquals(7, result.completedWorkouts());
        assertEquals(1, result.allByType().cardioWorkouts());
        assertEquals(1, result.completedByType().yogaWorkouts());

        assertNotNull(result.lastWorkout());
        assertEquals(1L, result.lastWorkout().id());

        assertNotNull(result.nextWorkout());
        assertEquals(2L, result.nextWorkout().id());
    }

    @Test
    void getGoalsStats_ShouldReturnCorrectStats() {
        long userId = 2L;

        when(goalService.countGoals(userId)).thenReturn(5);
        when(goalService.countGoals(userId, true)).thenReturn(3);

        User user = new User(); user.setId(userId);
        Goal goal = new Goal();
        goal.setId(10L);
        goal.setAuthor(user);
        goal.setText("Test Goal");
        goal.setDone(false);
        goal.setDeadline(LocalDateTime.now().plusDays(7));
        goal.setCreatedAt(LocalDateTime.now());

        when(goalService.getNearestDeadlineGoal(userId)).thenReturn(goal);

        StatsGoalDTO result = statsService.getGoalsStats(userId);

        assertEquals(5, result.totalGoals());
        assertEquals(3, result.completedGoals());
        assertNotNull(result.nearestGoal());
        assertEquals("Test Goal", result.nearestGoal().text());
    }

    @Test
    void getWorkoutsStats_ShouldHandleNullWorkouts() {
        long userId = 3L;

        when(workoutService.countWorkouts(userId)).thenReturn(0);
        when(workoutService.countWorkouts(userId, true)).thenReturn(0);

        for (WorkoutType type : WorkoutType.values()) {
            when(workoutService.countWorkouts(userId, type)).thenReturn(0);
            when(workoutService.countWorkouts(userId, type, true)).thenReturn(0);
        }

        when(workoutService.getLastCompletedWorkout(userId)).thenReturn(null);
        when(workoutService.getFirstUncompletedWorkout(userId)).thenReturn(null);

        StatsWorkoutDTO result = statsService.getWorkoutsStats(userId);

        assertEquals(0, result.totalWorkouts());
        assertNull(result.lastWorkout());
        assertNull(result.nextWorkout());
    }

    @Test
    void getGoalsStats_ShouldHandleNullGoal() {
        long userId = 4L;

        when(goalService.countGoals(userId)).thenReturn(0);
        when(goalService.countGoals(userId, true)).thenReturn(0);
        when(goalService.getNearestDeadlineGoal(userId)).thenReturn(null);

        StatsGoalDTO result = statsService.getGoalsStats(userId);

        assertEquals(0, result.totalGoals());
        assertEquals(0, result.completedGoals());
        assertNull(result.nearestGoal());
    }
}
