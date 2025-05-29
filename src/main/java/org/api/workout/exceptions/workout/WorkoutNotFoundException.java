package org.api.workout.exceptions.workout;

public class WorkoutNotFoundException extends RuntimeException {
    public WorkoutNotFoundException(String message) {
        super(message);
    }
}
