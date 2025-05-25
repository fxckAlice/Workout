package org.api.workout.services.workout;

import org.api.workout.enteties.workout.Workout;
import org.api.workout.repositories.workout.WorkoutDBRepo;
import org.springframework.stereotype.Service;

@Service
public class WorkoutDBService {
    private final WorkoutDBRepo workoutDBRepo;
    public WorkoutDBService(WorkoutDBRepo workoutDBRepo) {
        this.workoutDBRepo = workoutDBRepo;
    }
    public Workout findById(long id) {
        return workoutDBRepo.findById(id);
    }
    public int countAllByAuthorIdAndIsDone(long authorId, boolean isDone) {
        return workoutDBRepo.countAllByAuthorIdAndIsDone(authorId, isDone);
    }
    public void deleteById(long id) {
        workoutDBRepo.deleteById(id);
    }
    public Workout save(Workout workout) {
        return workoutDBRepo.save(workout);
    }
}
