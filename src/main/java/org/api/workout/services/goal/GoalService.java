package org.api.workout.services.goal;

import org.api.workout.dto.goal.GoalDTO;
import org.api.workout.dto.goal.GoalsFilterDTO;
import org.api.workout.dto.goal.NewGoalDTO;
import org.api.workout.dto.goal.UpdateGoalDTO;
import org.api.workout.exceptions.goals.GoalNotFoundException;
import org.api.workout.exceptions.workout.AccessForbiddenException;
import org.api.workout.entities.goals.Goal;
import org.api.workout.security.CustomUserDetails;
import org.api.workout.services.user.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GoalService {
    private final GoalDBService goalDBService;
    private final UserService userService;

    public GoalService(GoalDBService goalDBService, UserService userService) {
        this.goalDBService = goalDBService;
        this.userService = userService;
    }
    @SuppressWarnings("all")
    public List<GoalDTO> findAllByFilter(GoalsFilterDTO filter, CustomUserDetails userDetails) {
        List<Goal> goals;

        boolean hasAuthorId = filter.authorId() != null && !filter.authorId().isBlank();
        boolean hasIsDone = filter.isDone() != null;
        boolean hasDeadlineDateFrom = filter.deadlineDateFrom() != null;
        boolean hasDeadlineDateTo = filter.deadlineDateTo() != null;
        boolean hasCreatedAtDateFrom = filter.createdAtFrom() != null;
        boolean hasCreatedAtDateTo = filter.createdAtTo() != null;

        Boolean isDone = hasIsDone ? Boolean.parseBoolean(filter.isDone()) : null;
        Long authorId = null;
        if (hasAuthorId) {
            try {
                authorId = Long.parseLong(filter.authorId());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid authorId: " + filter.authorId());
            }
        }
        if (authorId != null && userDetails.getId() != authorId && userDetails.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessForbiddenException("You don't have access to this goal.");
        }

        LocalDateTime deadlineDateFrom = null;
        LocalDateTime deadlineDateTo = null;
        if (hasDeadlineDateFrom){
            deadlineDateFrom = filter.deadlineDateFrom();
            if(hasDeadlineDateTo){
                deadlineDateTo = filter.deadlineDateTo();
            }
            else throw new IllegalArgumentException("Both parameters 'deadlineDateFrom' and 'deadlineDateTo' must be specified. 'deadlineDateTo' is missing.'");
        }
        else if (hasDeadlineDateTo){
            throw new IllegalArgumentException("Both parameters 'deadlineDateFrom' and 'deadlineDateTo' must be specified. 'deadlineDateFrom' is missing.'");
        }
        LocalDateTime createdAtDateFrom = null;
        LocalDateTime createdAtDateTo = null;
        if (hasCreatedAtDateFrom){
            createdAtDateFrom = filter.createdAtFrom();
            if(hasCreatedAtDateTo){
                createdAtDateTo = filter.createdAtTo();
            }
            else throw new IllegalArgumentException("Both parameters 'createdAtFrom' and 'createdAtTo' must be specified. 'createdAtTo' is missing.'");
        }
        else if (hasCreatedAtDateTo){
            throw new IllegalArgumentException("Both parameters 'createdAtFrom' and 'createdAtTo' must be specified. 'createdAtFrom' is missing.'");
        }

        if (hasAuthorId && hasIsDone && hasDeadlineDateFrom && hasCreatedAtDateFrom) {
            goals = goalDBService.findAllByAuthorIdAndIsDoneAndDeadlineBetweenAndCreatedAtBetween(authorId, isDone, deadlineDateFrom, deadlineDateTo, createdAtDateFrom, createdAtDateTo);
        }
        else if (hasAuthorId && hasIsDone && hasCreatedAtDateFrom) {
            goals = goalDBService.findAllByAuthorIdAndIsDoneAndCreatedAtBetween(authorId, isDone, createdAtDateFrom, createdAtDateTo);
        }
        else if (hasAuthorId && hasIsDone && hasDeadlineDateFrom) {
            goals = goalDBService.findAllByAuthorIdAndIsDoneAndDeadlineBetween(authorId, isDone, deadlineDateFrom, deadlineDateTo);
        }
        else if (hasIsDone && hasDeadlineDateFrom && hasCreatedAtDateFrom) {
            goals = goalDBService.findAllByIsDoneAndDeadlineBetweenAndCreatedAtBetween(isDone, deadlineDateFrom, deadlineDateTo, createdAtDateFrom, createdAtDateTo);
        }
        else if (hasAuthorId && hasDeadlineDateFrom && hasCreatedAtDateFrom) {
            goals = goalDBService.findAllByAuthorIdAndDeadlineBetweenAndCreatedAtBetween(authorId, deadlineDateFrom, deadlineDateTo, createdAtDateFrom, createdAtDateTo);
        }
        else if (hasAuthorId && hasIsDone) {
            goals = goalDBService.findAllByAuthorIdAndIsDone(authorId, isDone);
        }
        else if (hasAuthorId && hasDeadlineDateFrom) {
            goals = goalDBService.findAllByAuthorIdAndDeadlineBetween(authorId, deadlineDateFrom, deadlineDateTo);
        }
        else if (hasAuthorId && hasCreatedAtDateFrom) {
            goals = goalDBService.findAllByAuthorIdAndCreatedAtBetween(authorId, createdAtDateFrom, createdAtDateTo);
        }
        else if (hasIsDone && hasDeadlineDateFrom) {
            goals = goalDBService.findAllByIsDoneAndDeadlineBetween(isDone, deadlineDateFrom, deadlineDateTo);
        }
        else if (hasIsDone && hasCreatedAtDateFrom) {
            goals = goalDBService.findAllByIsDoneAndCreatedAtBetween(isDone, createdAtDateFrom, createdAtDateTo);
        }
        else if (hasDeadlineDateFrom && hasCreatedAtDateFrom) {
            goals = goalDBService.findAllByDeadlineBetweenAndCreatedAtBetween(deadlineDateFrom, deadlineDateTo, createdAtDateFrom, createdAtDateTo);
        }
        else if (hasAuthorId) {
            goals = goalDBService.findAllByAuthorId(authorId);
        }
        else if (hasIsDone) {
            goals = goalDBService.findAllByIsDone(isDone);
        }
        else if (hasDeadlineDateFrom) {
            goals = goalDBService.findAllByDeadlineBetween(deadlineDateFrom, deadlineDateTo);
        }
        else if (hasCreatedAtDateFrom) {
            goals = goalDBService.findAllByCreatedAtBetween(createdAtDateFrom, createdAtDateTo);
        }
        else {
            goals = goalDBService.findAll();
        }

        return goals.stream()
                .map(goal -> new GoalDTO(
                        goal.getId(),
                        goal.getAuthor().getId(),
                        goal.getText(),
                        goal.isDone(),
                        goal.getDeadline(),
                        goal.getCreatedAt()
                )).toList();
    }
    public GoalDTO findGoalById(long id, CustomUserDetails userDetails) {
        Goal goal = goalDBService.findById(id);
        if (goal.getAuthor().getId() != userDetails.getId() && userDetails.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessForbiddenException("You don't have access to this goal.");
        }
        return new GoalDTO(
                goal.getId(),
                goal.getAuthor().getId(),
                goal.getText(),
                goal.isDone(),
                goal.getDeadline(),
                goal.getCreatedAt()
        );
    }
    public Goal newGoal(NewGoalDTO newGoalDTO, CustomUserDetails userDetails) {
        if (newGoalDTO.text() == null) {
            throw new IllegalArgumentException("Parameter 'text' is required.");
        }
        Goal goal = new Goal(
                userService.findById(userDetails.getId()),
                newGoalDTO.text()
        );
        if (newGoalDTO.deadline() != null) {
            goal.setDeadline(newGoalDTO.deadline());
        }
        return goalDBService.save(goal);
    }
    public GoalDTO updateGoal(long id, UpdateGoalDTO goalDTO, CustomUserDetails userDetails) {
        Goal goal = goalDBService.findById(id);
        if (goal.getAuthor().getId() != userDetails.getId() &&  userDetails.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessForbiddenException("You don't have access to this goal.");
        }
        if (goalDTO.text() == null) {
            throw new IllegalArgumentException("Parameter 'text' is required.");
        }
        if (goalDTO.deadline() == null) {
            throw new IllegalArgumentException("Parameter 'deadline' is required.");
        }
        goal.setText(goalDTO.text());
        goal.setDone(goalDTO.isDone());
        goal.setDeadline(goalDTO.deadline());
        goal = goalDBService.save(goal);
        return new GoalDTO(
                goal.getId(),
                goal.getAuthor().getId(),
                goal.getText(),
                goal.isDone(),
                goal.getDeadline(),
                goal.getCreatedAt()
        );
    }
    public void deleteGoal(long id, CustomUserDetails userDetails) {
        Goal goal = goalDBService.findById(id);
        if (goal.getAuthor().getId() != userDetails.getId() &&  userDetails.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessForbiddenException("You don't have access to this goal.");
        }
        goalDBService.deleteById(id);
    }
    public int countGoals(long authorId) {
        return goalDBService.countAllByAuthorId(authorId);
    }
    public int countGoals(long authorId, boolean isDone) {
        return goalDBService.countByAuthorIdAndIsDone(authorId, isDone);
    }
    public Goal getNearestDeadlineGoal(long authorId) {
        Goal nearestDeadlineGoal;
        try{
            nearestDeadlineGoal = goalDBService.findTopByAuthorIdAndDeadlineGreaterThanEqualOrderByDeadlineAsc(authorId);
        } catch (GoalNotFoundException e) {
            nearestDeadlineGoal = null;
        }
        return nearestDeadlineGoal;
    }

}
