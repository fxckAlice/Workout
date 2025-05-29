package org.api.workout.exceptions.goals;

public class GoalNotFoundException extends RuntimeException {
    public GoalNotFoundException(String message) {
        super(message);
    }
}
