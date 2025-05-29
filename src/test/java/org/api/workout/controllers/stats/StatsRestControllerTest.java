package org.api.workout.controllers.stats;

import org.api.workout.dto.goal.GoalDTO;
import org.api.workout.dto.stats.StatsByTypeDTO;
import org.api.workout.dto.stats.StatsGoalDTO;
import org.api.workout.dto.stats.StatsWorkoutDTO;
import org.api.workout.dto.workout.WorkoutDTO;
import org.api.workout.entities.workout.WorkoutType;
import org.api.workout.security.CustomUserDetails;
import org.api.workout.services.stats.StatsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StatsRestControllerTest {

    @Mock
    private StatsService statsService;

    @InjectMocks
    private StatsRestController statsRestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getWorkoutsStats_ShouldReturnWorkoutStats() {
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getId()).thenReturn(1L);

        StatsByTypeDTO byType = new StatsByTypeDTO(1, 2, 3, 4, 5, 6);
        StatsByTypeDTO completedByType = new StatsByTypeDTO(0, 1, 0, 1, 0, 0);
        WorkoutDTO last = new WorkoutDTO(1L, 1L, LocalDateTime.now(), true, WorkoutType.CARDIO, 30, LocalDateTime.now());
        WorkoutDTO nearest = new WorkoutDTO(2L, 1L, LocalDateTime.now().plusDays(1), false, WorkoutType.STRENGTH, 45, LocalDateTime.now());

        StatsWorkoutDTO expected = new StatsWorkoutDTO(1L, 10, 6, byType, completedByType, last, nearest);
        when(statsService.getWorkoutsStats(1L)).thenReturn(expected);

        ResponseEntity<?> response = statsRestController.getWorkoutsStats(userDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(statsService).getWorkoutsStats(1L);
    }

    @Test
    void getGoalsStats_ShouldReturnGoalStats() {
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getId()).thenReturn(2L);

        GoalDTO nearestGoal = new GoalDTO(10L, 2L, "Run 5k", false, LocalDateTime.now().plusDays(5), LocalDateTime.now());
        StatsGoalDTO expected = new StatsGoalDTO(2L, 5, 3, nearestGoal);
        when(statsService.getGoalsStats(2L)).thenReturn(expected);

        ResponseEntity<?> response = statsRestController.getGoalsStats(userDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(statsService).getGoalsStats(2L);
    }
}
