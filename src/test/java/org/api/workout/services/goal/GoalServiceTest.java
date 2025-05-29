package org.api.workout.services.goal;

import org.api.workout.dto.goal.GoalDTO;
import org.api.workout.dto.goal.NewGoalDTO;
import org.api.workout.dto.goal.UpdateGoalDTO;
import org.api.workout.entities.goals.Goal;
import org.api.workout.entities.user.User;
import org.api.workout.exceptions.goals.GoalNotFoundException;
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
class GoalServiceTest {

    @Mock
    GoalDBService goalDBService;

    @Mock
    UserService userService;

    @InjectMocks
    GoalService goalService;

    @Test
    void findGoalById_shouldReturnDTO_whenAuthorMatches() {
        User author = new User(1L, "user", "pass");
        Goal goal = new Goal();
        goal.setId(1L);
        goal.setAuthor(author);
        goal.setText("Test goal");
        goal.setDone(false);
        goal.setDeadline(LocalDateTime.now().plusDays(1));
        goal.setCreatedAt(LocalDateTime.now());

        CustomUserDetails userDetails = new CustomUserDetails(
                1L, "user", "pass",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        Mockito.when(goalDBService.findById(1L)).thenReturn(goal);

        GoalDTO dto = goalService.findGoalById(1L, userDetails);

        assertEquals(1L, dto.id());
        assertEquals(author.getId(), dto.authorId());
        assertEquals("Test goal", dto.text());
    }

    @Test
    void findGoalById_shouldReturnDTO_whenUserIsAdmin() {
        User author = new User(2L, "other", "pass");
        Goal goal = new Goal();
        goal.setId(1L);
        goal.setAuthor(author);

        CustomUserDetails admin = new CustomUserDetails(
                1L, "admin", "pass",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        Mockito.when(goalDBService.findById(1L)).thenReturn(goal);

        GoalDTO dto = goalService.findGoalById(1L, admin);

        assertEquals(1L, dto.id());
    }

    @Test
    void findGoalById_shouldThrow_whenNoAccess() {
        User author = new User(2L, "other", "pass");
        Goal goal = new Goal();
        goal.setAuthor(author);

        CustomUserDetails user = new CustomUserDetails(
                1L, "user", "pass",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        Mockito.when(goalDBService.findById(1L)).thenReturn(goal);

        assertThrows(AccessForbiddenException.class, () ->
                goalService.findGoalById(1L, user));
    }

    @Test
    void newGoal_shouldCreateGoalSuccessfully() {
        NewGoalDTO dto = new NewGoalDTO("New goal text", LocalDateTime.now().plusDays(5));
        User user = new User(1L, "user", "pass");

        Mockito.when(userService.findById(1L)).thenReturn(user);
        Mockito.when(goalDBService.save(Mockito.any())).thenAnswer(i -> i.getArgument(0));

        CustomUserDetails userDetails = new CustomUserDetails(
                1L, "user", "pass",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        Goal goal = goalService.newGoal(dto, userDetails);

        assertEquals(user, goal.getAuthor());
        assertEquals(dto.text(), goal.getText());
        assertEquals(dto.deadline(), goal.getDeadline());
    }

    @Test
    void newGoal_shouldThrow_whenTextIsNull() {
        NewGoalDTO dto = new NewGoalDTO(null, LocalDateTime.now());

        CustomUserDetails userDetails = new CustomUserDetails(
                1L, "user", "pass",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        assertThrows(IllegalArgumentException.class, () ->
                goalService.newGoal(dto, userDetails));
    }

    @Test
    void updateGoal_shouldUpdateSuccessfully() {
        Goal goal = new Goal();
        goal.setAuthor(new User(1L, "user", "pass"));
        goal.setId(1L);
        goal.setText("Old text");
        goal.setDeadline(LocalDateTime.now().plusDays(1));
        goal.setDone(false);

        UpdateGoalDTO dto = new UpdateGoalDTO("Updated text", true, LocalDateTime.now().plusDays(10));

        CustomUserDetails user = new CustomUserDetails(
                1L, "user", "pass",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        Mockito.when(goalDBService.findById(1L)).thenReturn(goal);
        Mockito.when(goalDBService.save(Mockito.any())).thenAnswer(i -> i.getArgument(0));

        GoalDTO updated = goalService.updateGoal(1L, dto, user);

        assertEquals(dto.text(), updated.text());
        assertEquals(dto.isDone(), updated.isDone());
        assertEquals(dto.deadline(), updated.deadline());
    }

    @Test
    void updateGoal_shouldThrow_whenNoAccess() {
        Goal goal = new Goal();
        goal.setAuthor(new User(2L, "other", "pass"));

        UpdateGoalDTO dto = new UpdateGoalDTO("Updated text", true, LocalDateTime.now().plusDays(5));

        CustomUserDetails user = new CustomUserDetails(
                1L, "user", "pass",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        Mockito.when(goalDBService.findById(1L)).thenReturn(goal);

        assertThrows(AccessForbiddenException.class, () ->
                goalService.updateGoal(1L, dto, user));
    }

    @Test
    void updateGoal_shouldThrow_whenTextIsNull() {
        Goal goal = new Goal();
        goal.setAuthor(new User(1L, "user", "pass"));

        UpdateGoalDTO dto = new UpdateGoalDTO(null, true, LocalDateTime.now().plusDays(5));

        CustomUserDetails user = new CustomUserDetails(
                1L, "user", "pass",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        Mockito.when(goalDBService.findById(1L)).thenReturn(goal);

        assertThrows(IllegalArgumentException.class, () ->
                goalService.updateGoal(1L, dto, user));
    }

    @Test
    void updateGoal_shouldThrow_whenDeadlineIsNull() {
        Goal goal = new Goal();
        goal.setAuthor(new User(1L, "user", "pass"));

        UpdateGoalDTO dto = new UpdateGoalDTO("Text", true, null);

        CustomUserDetails user = new CustomUserDetails(
                1L, "user", "pass",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        Mockito.when(goalDBService.findById(1L)).thenReturn(goal);

        assertThrows(IllegalArgumentException.class, () ->
                goalService.updateGoal(1L, dto, user));
    }

    @Test
    void deleteGoal_shouldSucceed_whenAuthor() {
        Goal goal = new Goal();
        goal.setId(1L);
        goal.setAuthor(new User(1L, "user", "pass"));

        CustomUserDetails user = new CustomUserDetails(
                1L, "user", "pass",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        Mockito.when(goalDBService.findById(1L)).thenReturn(goal);

        goalService.deleteGoal(1L, user);

        Mockito.verify(goalDBService).deleteById(1L);
    }

    @Test
    void deleteGoal_shouldThrow_whenNoAccess() {
        Goal goal = new Goal();
        goal.setAuthor(new User(2L, "other", "pass"));

        CustomUserDetails user = new CustomUserDetails(
                1L, "user", "pass",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        Mockito.when(goalDBService.findById(1L)).thenReturn(goal);

        assertThrows(AccessForbiddenException.class, () ->
                goalService.deleteGoal(1L, user));
    }

    @Test
    void countGoals_shouldReturnCount() {
        Mockito.when(goalDBService.countAllByAuthorId(1L)).thenReturn(5);

        int count = goalService.countGoals(1L);

        assertEquals(5, count);
    }

    @Test
    void countGoalsWithIsDone_shouldReturnCount() {
        Mockito.when(goalDBService.countByAuthorIdAndIsDone(1L, true)).thenReturn(3);

        int count = goalService.countGoals(1L, true);

        assertEquals(3, count);
    }

    @Test
    void getNearestDeadlineGoal_shouldReturnGoal() {
        Goal goal = new Goal();
        goal.setId(1L);

        Mockito.when(goalDBService.findTopByAuthorIdAndDeadlineGreaterThanEqualOrderByDeadlineAsc(1L)).thenReturn(goal);

        Goal result = goalService.getNearestDeadlineGoal(1L);

        assertEquals(goal, result);
    }

    @Test
    void getNearestDeadlineGoal_shouldReturnNull_whenNotFound() {
        Mockito.when(goalDBService.findTopByAuthorIdAndDeadlineGreaterThanEqualOrderByDeadlineAsc(1L))
                .thenThrow(new GoalNotFoundException("Not found"));

        Goal result = goalService.getNearestDeadlineGoal(1L);

        assertNull(result);
    }
}
