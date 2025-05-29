package org.api.workout.controllers.goals;

import org.api.workout.controllers.dto.goal.GoalDTO;
import org.api.workout.controllers.dto.goal.GoalsFilterDTO;
import org.api.workout.controllers.dto.goal.NewGoalDTO;
import org.api.workout.entities.goals.Goal;
import org.api.workout.security.CustomUserDetails;
import org.api.workout.services.goal.GoalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalsRestController {
    private final GoalService goalService;

    public GoalsRestController(GoalService goalService) {
        this.goalService = goalService;
    }
    @SuppressWarnings("unused")
    @PreAuthorize("hasRole('ADMIN') or #userDetails.id.toString() == goalsFilterDTO.authorId()")
    @GetMapping("")
    public ResponseEntity<?> getGoals(@ModelAttribute GoalsFilterDTO goalsFilterDTO, @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<GoalDTO> goalDTO = goalService.findAllByFilter(goalsFilterDTO);
        return new ResponseEntity<>(goalDTO, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getGoal(@PathVariable long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        GoalDTO goalDTO = goalService.findGoalById(id, userDetails);
        return new ResponseEntity<>(goalDTO, HttpStatus.OK);
    }
    @PostMapping("")
    public ResponseEntity<?> postGoal(@RequestBody NewGoalDTO newGoalDTO, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Goal goal = goalService.newGoal(newGoalDTO, userDetails);
        return new ResponseEntity<>(goal.getId(), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> putGoal(@PathVariable long id, @RequestBody GoalDTO goalDTO, @AuthenticationPrincipal CustomUserDetails userDetails) {
        GoalDTO updatedGoalDTO = goalService.updateGoal(id, goalDTO, userDetails);
        return new ResponseEntity<>(updatedGoalDTO, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGoal(@PathVariable long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        goalService.deleteGoal(id, userDetails);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
