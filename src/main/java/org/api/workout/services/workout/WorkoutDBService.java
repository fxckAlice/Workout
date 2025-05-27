package org.api.workout.services.workout;

import org.api.workout.enteties.workout.Workout;
import org.api.workout.repositories.workout.WorkoutDBRepo;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
public class WorkoutDBService {
    private final WorkoutDBRepo workoutDBRepo;
    protected WorkoutDBService(WorkoutDBRepo workoutDBRepo) {
        this.workoutDBRepo = workoutDBRepo;
    }
    protected Workout findById(long id) {
        return workoutDBRepo.findById(id);
    }
    protected int countAllByAuthorIdAndIsDone(long authorId, boolean isDone) {
        return workoutDBRepo.countAllByAuthorIdAndIsDone(authorId, isDone);
    }
    protected void deleteById(long id) {
        workoutDBRepo.deleteById(id);
    }
    protected Workout save(Workout workout) {
        return workoutDBRepo.save(workout);
    }
}
