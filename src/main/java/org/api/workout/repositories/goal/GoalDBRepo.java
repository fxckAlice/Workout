package org.api.workout.repositories.goal;

import org.api.workout.entities.goals.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalDBRepo extends JpaRepository<Goal, Long> {
    Goal findById(long id);
    int countAllByAuthorId(long authorId);
    void deleteById(long id);
}
