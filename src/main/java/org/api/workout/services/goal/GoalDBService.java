package org.api.workout.services.goal;

import org.api.workout.enteties.goals.Goal;
import org.api.workout.repositories.goal.GoalDBRepo;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
public class GoalDBService {
    private final GoalDBRepo goalDBRepo;
    protected GoalDBService(GoalDBRepo goalDBRepo) {
        this.goalDBRepo = goalDBRepo;
    }
    protected Goal findById(long id) {
        return goalDBRepo.findById(id);
    }
    protected int countAllByAuthorId(long authorId) {
        return goalDBRepo.countAllByAuthorId(authorId);
    }
    protected void deleteById(long id) {
        goalDBRepo.deleteById(id);
    }
    protected Goal save(Goal goal) {
        return goalDBRepo.save(goal);
    }
}
