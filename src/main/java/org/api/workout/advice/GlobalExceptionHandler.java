package org.api.workout.advice;

import org.api.workout.exceptions.goals.GoalNotFoundException;
import org.api.workout.exceptions.user.UserAlreadyExistsException;
import org.api.workout.exceptions.user.UserNotFoundException;
import org.api.workout.exceptions.workout.AccessForbiddenException;
import org.api.workout.exceptions.workout.WorkoutNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "not found");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "conflict");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "bad request");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessForbiddenException.class)
    public ResponseEntity<?> handleAccessForbiddenException(AccessForbiddenException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "forbidden");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(WorkoutNotFoundException.class)
    public ResponseEntity<?> handleWorkoutNotFoundException(WorkoutNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "not found");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(GoalNotFoundException.class)
    public ResponseEntity<?> handleGoalNotFoundException(GoalNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "not found");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

}
