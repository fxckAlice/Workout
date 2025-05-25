package org.api.workout.services.goal;

import org.api.workout.enteties.goals.Goal;
import org.api.workout.repositories.goal.GoalDBRepo;
import org.springframework.stereotype.Service;

@Service
public class GoalDBService {
    private final GoalDBRepo goalDBRepo;
    public GoalDBService(GoalDBRepo goalDBRepo) {
        this.goalDBRepo = goalDBRepo;
    }
    public Goal findById(long id) {
        return goalDBRepo.findById(id);
    }
    public int countAllByAuthorId(long authorId) {
        return goalDBRepo.countAllByAuthorId(authorId);
    }
    public void deleteById(long id) {
        goalDBRepo.deleteById(id);
    }
    public Goal save(Goal goal) {
        return goalDBRepo.save(goal);
    }
}
