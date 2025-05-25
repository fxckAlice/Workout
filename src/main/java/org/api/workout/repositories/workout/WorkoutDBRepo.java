package org.api.workout.repositories.workout;

import org.api.workout.enteties.workout.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutDBRepo extends JpaRepository<Workout, Long> {
    Workout findById(long id);
    int countAllByAuthorIdAndIsDone(long authorId, boolean isDone);
    void deleteById(long id);
}
