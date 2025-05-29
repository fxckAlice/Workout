package org.api.workout.controllers.stats;

import org.api.workout.controllers.dto.stats.StatsGoalDTO;
import org.api.workout.controllers.dto.stats.StatsWorkoutDTO;
import org.api.workout.security.CustomUserDetails;
import org.api.workout.services.stats.StatsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
public class StatsRestController {
    private final StatsService statsService;

    public StatsRestController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/workouts")
    public ResponseEntity<?> getWorkoutsStats(@AuthenticationPrincipal CustomUserDetails userDetails) {
        StatsWorkoutDTO statsWorkoutDTO = statsService.getWorkoutsStats(userDetails.getId());
        return new ResponseEntity<>(statsWorkoutDTO, HttpStatus.OK);
    }

    @GetMapping("/goals")
    public ResponseEntity<?> getGoalsStats(@AuthenticationPrincipal CustomUserDetails userDetails) {
        StatsGoalDTO statsGoalDTO = statsService.getGoalsStats(userDetails.getId());
        return new ResponseEntity<>(statsGoalDTO, HttpStatus.OK);
    }
}
