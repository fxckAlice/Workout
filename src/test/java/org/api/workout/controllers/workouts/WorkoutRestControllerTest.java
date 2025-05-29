package org.api.workout.controllers.workouts;

import org.api.workout.dto.workout.*;
import org.api.workout.entities.workout.Workout;
import org.api.workout.security.CustomUserDetails;
import org.api.workout.services.workout.WorkoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkoutRestControllerTest {

    @Mock
    private WorkoutService workoutService;

    @InjectMocks
    private WorkoutRestController workoutRestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getWorkouts_ShouldReturnListOfWorkouts() {
        WorkoutFilterDTO filterDTO = new WorkoutFilterDTO(null, null, null, null, null);
        CustomUserDetails userDetails = mock(CustomUserDetails.class);

        List<WorkoutDTO> expectedList = List.of(
                new WorkoutDTO(1L, 1L, LocalDateTime.now(), true, null, 60, LocalDateTime.now())
        );

        when(workoutService.findWorkoutsByFilter(filterDTO, userDetails)).thenReturn(expectedList);

        ResponseEntity<?> response = workoutRestController.getWorkouts(filterDTO, userDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedList, response.getBody());
        verify(workoutService).findWorkoutsByFilter(filterDTO, userDetails);
    }

    @Test
    void postWorkout_ShouldReturnWorkoutId() {
        NewWorkoutDTO newWorkout = new NewWorkoutDTO(null, null, 30);
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getId()).thenReturn(99L);

        Workout mockWorkout = new Workout();
        mockWorkout.setId(42L);

        when(workoutService.newWorkout(newWorkout, 99L)).thenReturn(mockWorkout);

        ResponseEntity<?> response = workoutRestController.postWorkout(newWorkout, userDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(42L, response.getBody());
        verify(workoutService).newWorkout(newWorkout, 99L);
    }

    @Test
    void getWorkout_ShouldReturnWorkoutDTO() {
        long workoutId = 5L;
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        WorkoutDTO expectedDTO = new WorkoutDTO(workoutId, 1L, LocalDateTime.now(), false, null, 45, LocalDateTime.now());

        when(workoutService.findWorkoutById(workoutId, userDetails)).thenReturn(expectedDTO);

        ResponseEntity<?> response = workoutRestController.getWorkout(workoutId, userDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDTO, response.getBody());
        verify(workoutService).findWorkoutById(workoutId, userDetails);
    }

    @Test
    void putWorkout_ShouldReturnUpdatedWorkoutDTO() {
        long workoutId = 7L;
        UpdateWorkoutDTO updateDTO = new UpdateWorkoutDTO(null, true, null, 55);
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        WorkoutDTO updatedDTO = new WorkoutDTO(workoutId, 1L, LocalDateTime.now(), true, null, 55, LocalDateTime.now());

        when(workoutService.updateWorkout(workoutId, updateDTO, userDetails)).thenReturn(updatedDTO);

        ResponseEntity<?> response = workoutRestController.putWorkout(workoutId, updateDTO, userDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDTO, response.getBody());
        verify(workoutService).updateWorkout(workoutId, updateDTO, userDetails);
    }

    @Test
    void deleteWorkout_ShouldReturnStatusOk() {
        long workoutId = 10L;
        CustomUserDetails userDetails = mock(CustomUserDetails.class);

        ResponseEntity<?> response = workoutRestController.deleteWorkout(workoutId, userDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(workoutService).deleteWorkout(workoutId, userDetails);
    }
}
