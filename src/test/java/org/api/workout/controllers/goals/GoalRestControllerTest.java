package org.api.workout.controllers.goals;

import org.api.workout.dto.goal.*;
import org.api.workout.entities.goals.Goal;
import org.api.workout.security.CustomUserDetails;
import org.api.workout.services.goal.GoalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GoalsRestControllerTest {

    @Mock
    private GoalService goalService;

    @InjectMocks
    private GoalsRestController goalsRestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getGoals_ShouldReturnListOfGoals() {
        GoalsFilterDTO filterDTO = new GoalsFilterDTO(null, null, null, null, null, null);
        CustomUserDetails userDetails = mock(CustomUserDetails.class);

        List<GoalDTO> expected = List.of(
                new GoalDTO(1L, 1L, "Test goal", false, LocalDateTime.now(), LocalDateTime.now())
        );

        when(goalService.findAllByFilter(filterDTO, userDetails)).thenReturn(expected);

        ResponseEntity<?> response = goalsRestController.getGoals(filterDTO, userDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(goalService).findAllByFilter(filterDTO, userDetails);
    }

    @Test
    void getGoal_ShouldReturnGoalDTO() {
        long goalId = 10L;
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        GoalDTO expectedDTO = new GoalDTO(goalId, 1L, "Some goal", false, LocalDateTime.now(), LocalDateTime.now());

        when(goalService.findGoalById(goalId, userDetails)).thenReturn(expectedDTO);

        ResponseEntity<?> response = goalsRestController.getGoal(goalId, userDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDTO, response.getBody());
        verify(goalService).findGoalById(goalId, userDetails);
    }

    @Test
    void postGoal_ShouldReturnCreatedStatusAndId() {
        NewGoalDTO newGoalDTO = new NewGoalDTO("Goal", LocalDateTime.now());
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        Goal savedGoal = new Goal();
        savedGoal.setId(77L);

        when(goalService.newGoal(newGoalDTO, userDetails)).thenReturn(savedGoal);

        ResponseEntity<?> response = goalsRestController.postGoal(newGoalDTO, userDetails);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(77L, response.getBody());
        verify(goalService).newGoal(newGoalDTO, userDetails);
    }

    @Test
    void putGoal_ShouldReturnUpdatedDTO() {
        long goalId = 5L;
        UpdateGoalDTO updateDTO = new UpdateGoalDTO("Updated goal", true, LocalDateTime.now());
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        GoalDTO updatedDTO = new GoalDTO(goalId, 1L, "Updated goal", true, LocalDateTime.now(), LocalDateTime.now());

        when(goalService.updateGoal(goalId, updateDTO, userDetails)).thenReturn(updatedDTO);

        ResponseEntity<?> response = goalsRestController.putGoal(goalId, updateDTO, userDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDTO, response.getBody());
        verify(goalService).updateGoal(goalId, updateDTO, userDetails);
    }

    @Test
    void deleteGoal_ShouldReturnStatusOk() {
        long goalId = 22L;
        CustomUserDetails userDetails = mock(CustomUserDetails.class);

        ResponseEntity<?> response = goalsRestController.deleteGoal(goalId, userDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(goalService).deleteGoal(goalId, userDetails);
    }
}
