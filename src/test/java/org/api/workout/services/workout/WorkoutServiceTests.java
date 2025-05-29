package org.api.workout.services.workout;

import org.api.workout.dto.workout.NewWorkoutDTO;
import org.api.workout.dto.workout.UpdateWorkoutDTO;
import org.api.workout.dto.workout.WorkoutDTO;
import org.api.workout.entities.user.User;
import org.api.workout.entities.workout.Workout;
import org.api.workout.entities.workout.WorkoutType;
import org.api.workout.exceptions.workout.AccessForbiddenException;
import org.api.workout.security.CustomUserDetails;
import org.api.workout.services.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceTest {

    @Mock
    WorkoutDBService workoutDBService;

    @Mock
    UserService userService;

    @InjectMocks
    WorkoutService workoutService;

    @Test
    void findWorkoutById_shouldReturnDTO_whenAuthorMatches() {
        User author = new User(1L, "user", "pass");
        Workout workout = new Workout();
        workout.setId(1L);
        workout.setAuthor(author);

        CustomUserDetails userDetails = new CustomUserDetails(
                1L, "user", "pass",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        Mockito.when(workoutDBService.findById(1L)).thenReturn(workout);

        WorkoutDTO dto = workoutService.findWorkoutById(1L, userDetails);

        assertEquals(1L, dto.id());
        assertEquals(1L, dto.authorId());
    }

    @Test
    void findWorkoutById_shouldReturnDTO_whenUserIsAdmin() {
        User author = new User(2L, "other", "pass");
        Workout workout = new Workout();
        workout.setId(1L);
        workout.setAuthor(author);

        CustomUserDetails admin = new CustomUserDetails(
                1L, "admin", "pass",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        Mockito.when(workoutDBService.findById(1L)).thenReturn(workout);

        WorkoutDTO dto = workoutService.findWorkoutById(1L, admin);

        assertEquals(1L, dto.id());
    }

    @Test
    void findWorkoutById_shouldThrow_whenNoAccess() {
        User author = new User(2L, "other", "pass");
        Workout workout = new Workout();
        workout.setAuthor(author);

        CustomUserDetails user = new CustomUserDetails(
                1L, "user", "pass",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        Mockito.when(workoutDBService.findById(1L)).thenReturn(workout);

        assertThrows(AccessForbiddenException.class, () ->
                workoutService.findWorkoutById(1L, user));
    }

    @Test
    void newWorkout_shouldCreateWorkoutSuccessfully() {
        NewWorkoutDTO dto = new NewWorkoutDTO(LocalDateTime.now(), WorkoutType.CARDIO, 20);
        User user = new User(1L, "user", "pass");

        Mockito.when(userService.findById(1L)).thenReturn(user);
        Mockito.when(workoutDBService.save(Mockito.any())).thenAnswer(i -> i.getArgument(0));

        Workout workout = workoutService.newWorkout(dto, 1L);

        assertEquals(user, workout.getAuthor());
        assertEquals(dto.type(), workout.getType());
        assertEquals(dto.duration(), workout.getDuration());
    }

    @Test
    void newWorkout_shouldThrow_whenDurationTooShort() {
        NewWorkoutDTO dto = new NewWorkoutDTO(LocalDateTime.now(), WorkoutType.CARDIO, 10);

        assertThrows(IllegalArgumentException.class, () ->
                workoutService.newWorkout(dto, 1L));
    }

    @Test
    void updateWorkout_shouldUpdateSuccessfully() {
        Workout workout = new Workout();
        workout.setAuthor(new User(1L, "user", "pass"));
        workout.setId(1L);

        UpdateWorkoutDTO dto = new UpdateWorkoutDTO(
                LocalDateTime.now(), true, WorkoutType.STRENGTH, 30
        );

        CustomUserDetails user = new CustomUserDetails(
                1L, "user", "pass",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        Mockito.when(workoutDBService.findById(1L)).thenReturn(workout);
        Mockito.when(workoutDBService.save(Mockito.any())).thenAnswer(i -> i.getArgument(0));

        WorkoutDTO updated = workoutService.updateWorkout(1L, dto, user);

        assertEquals(dto.type(), updated.type());
        assertEquals(dto.duration(), updated.duration());
        assertTrue(updated.isDone());
    }

    @Test
    void updateWorkout_shouldThrow_whenNoAccess() {
        Workout workout = new Workout();
        workout.setAuthor(new User(2L, "other", "pass"));

        UpdateWorkoutDTO dto = new UpdateWorkoutDTO(
                LocalDateTime.now(), true, WorkoutType.CARDIO, 20
        );

        CustomUserDetails user = new CustomUserDetails(
                1L, "user", "pass",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        Mockito.when(workoutDBService.findById(1L)).thenReturn(workout);

        assertThrows(AccessForbiddenException.class, () ->
                workoutService.updateWorkout(1L, dto, user));
    }

    @Test
    void updateWorkout_shouldThrow_whenDurationTooShort() {
        Workout workout = new Workout();
        workout.setAuthor(new User(1L, "user", "pass"));

        UpdateWorkoutDTO dto = new UpdateWorkoutDTO(
                LocalDateTime.now(), false, WorkoutType.STRENGTH, 10
        );

        CustomUserDetails user = new CustomUserDetails(
                1L, "user", "pass",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        Mockito.when(workoutDBService.findById(1L)).thenReturn(workout);

        assertThrows(IllegalArgumentException.class, () ->
                workoutService.updateWorkout(1L, dto, user));
    }

    @Test
    void deleteWorkout_shouldSucceed_whenAuthor() {
        Workout workout = new Workout();
        workout.setId(1L);
        workout.setAuthor(new User(1L, "user", "pass"));

        CustomUserDetails user = new CustomUserDetails(
                1L, "user", "pass",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        Mockito.when(workoutDBService.findById(1L)).thenReturn(workout);

        workoutService.deleteWorkout(1L, user);

        Mockito.verify(workoutDBService).deleteById(1L);
    }

    @Test
    void deleteWorkout_shouldThrow_whenNoAccess() {
        Workout workout = new Workout();
        workout.setAuthor(new User(2L, "other", "pass"));

        CustomUserDetails user = new CustomUserDetails(
                1L, "user", "pass",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        Mockito.when(workoutDBService.findById(1L)).thenReturn(workout);

        assertThrows(AccessForbiddenException.class, () ->
                workoutService.deleteWorkout(1L, user));
    }
}
