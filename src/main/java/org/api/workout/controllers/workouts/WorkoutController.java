package org.api.workout.controllers.workouts;

import org.api.workout.controllers.dto.NewWorkoutDTO;
import org.api.workout.controllers.dto.WorkoutDTO;
import org.api.workout.controllers.dto.WorkoutFilterDTO;
import org.api.workout.entities.workout.Workout;
import org.api.workout.security.CustomUserDetails;
import org.api.workout.services.workout.WorkoutService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @SuppressWarnings("unused")
    @PreAuthorize("hasRole('ADMIN') or userDetails.id.toString() == workoutFilterDTO.authorId()")
    @GetMapping("")
    public ResponseEntity<?> getWorkouts(@ModelAttribute WorkoutFilterDTO workoutFilterDTO,  @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<WorkoutDTO> listOfWorkouts = workoutService.findWorkoutsByFilter(workoutFilterDTO);
        return new ResponseEntity<>(listOfWorkouts, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> postWorkout(@RequestBody NewWorkoutDTO workoutDTO, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Workout workout = workoutService.newWorkout(workoutDTO, userDetails.getId());
        return new ResponseEntity<>(workout.getId(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWorkout(@PathVariable long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        WorkoutDTO workoutDTO = workoutService.findWorkoutById(id, userDetails);
        return new ResponseEntity<>(workoutDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putWorkout(@PathVariable long id, @RequestBody WorkoutDTO workoutDTO, @AuthenticationPrincipal CustomUserDetails userDetails) {
        WorkoutDTO updatedWorkoutDTO = workoutService.updateWorkout(id, workoutDTO, userDetails);
        return new ResponseEntity<>(updatedWorkoutDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkout(@PathVariable long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        workoutService.deleteWorkout(id, userDetails);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
