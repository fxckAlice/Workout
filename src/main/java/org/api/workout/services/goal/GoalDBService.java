package org.api.workout.services.goal;

import org.api.workout.controllers.exceptions.goals.GoalNotFoundException;
import org.api.workout.entities.goals.Goal;
import org.api.workout.repositories.goal.GoalDBRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("unused")
@Service
public class GoalDBService {
    private final GoalDBRepo goalDBRepo;
    protected GoalDBService(GoalDBRepo goalDBRepo) {
        this.goalDBRepo = goalDBRepo;
    }

    protected Goal findById(long id) {
        return goalDBRepo.findById(id).orElseThrow(() -> new GoalNotFoundException("Goal with id: " + id + " not found."));
    }
    protected int countAllByAuthorId(long authorId) {
        return goalDBRepo.countAllByAuthorId(authorId);
    }
    protected int countByAuthorIdAndIsDone(long authorId, boolean isDone) {
        return goalDBRepo.countByAuthorIdAndIsDone(authorId, isDone);
    }
    protected Goal findTopByAuthorIdAndDeadlineGreaterThanEqualOrderByDeadlineAsc(long authorId) {
        return goalDBRepo.findTopByAuthorIdAndDeadlineGreaterThanEqualOrderByDeadlineAsc(authorId, LocalDateTime.now()).orElseThrow(() -> new GoalNotFoundException("Goal not found for authorId: " + authorId + " and deadline: " + LocalDateTime.now() + "."));
    }
    @SuppressWarnings( "UnusedReturnValue")
    protected void deleteById(long id) {
        goalDBRepo.deleteById(id);
    }
    protected List<Goal> findAll(){
        return goalDBRepo.findAll();
    }
    protected List<Goal> findAllByAuthorId(long authorId){
        return goalDBRepo.findAllByAuthorId(authorId);
    }
    protected List<Goal> findAllByIsDone(boolean isDone){
        return goalDBRepo.findAllByIsDone(isDone);
    }
    protected List<Goal> findAllByDeadlineBetween(LocalDateTime deadlineDateFrom, LocalDateTime deadlineDateTo){
        return goalDBRepo.findAllByDeadlineBetween(deadlineDateFrom, deadlineDateTo);
    }
    protected List<Goal> findAllByCreatedAtBetween(LocalDateTime createdAtDateFrom, LocalDateTime createdAtDateTo){
        return goalDBRepo.findAllByCreatedAtBetween(createdAtDateFrom, createdAtDateTo);
    }
    protected List<Goal> findAllByAuthorIdAndIsDone(long authorId, boolean isDone){
        return goalDBRepo.findAllByAuthorIdAndIsDone(authorId, isDone);
    }
    protected List<Goal> findAllByAuthorIdAndDeadlineBetween(long authorId, LocalDateTime deadlineDateFrom, LocalDateTime deadlineDateTo){
        return goalDBRepo.findAllByAuthorIdAndDeadlineBetween(authorId, deadlineDateFrom, deadlineDateTo);
    }
    protected List<Goal> findAllByAuthorIdAndCreatedAtBetween(long authorId, LocalDateTime createdAtDateFrom, LocalDateTime createdAtDateTo){
        return goalDBRepo.findAllByAuthorIdAndCreatedAtBetween(authorId, createdAtDateFrom, createdAtDateTo);
    }
    protected List<Goal> findAllByIsDoneAndDeadlineBetween(boolean isDone, LocalDateTime deadlineDateFrom, LocalDateTime deadlineDateTo){
        return goalDBRepo.findAllByIsDoneAndDeadlineBetween(isDone, deadlineDateFrom, deadlineDateTo);
    }
    protected List<Goal> findAllByIsDoneAndCreatedAtBetween(boolean isDone, LocalDateTime createdAtDateFrom, LocalDateTime createdAtDateTo){
        return goalDBRepo.findAllByIsDoneAndCreatedAtBetween(isDone, createdAtDateFrom, createdAtDateTo);
    }
    protected List<Goal> findAllByDeadlineBetweenAndCreatedAtBetween(LocalDateTime deadlineDateFrom, LocalDateTime deadlineDateTo, LocalDateTime createdAtDateFrom, LocalDateTime createdAtDateTo){
        return goalDBRepo.findAllByDeadlineBetweenAndCreatedAtBetween(deadlineDateFrom, deadlineDateTo, createdAtDateFrom, createdAtDateTo);
    }
    protected List<Goal> findAllByAuthorIdAndIsDoneAndDeadlineBetween(long authorId, boolean isDone, LocalDateTime deadlineDateFrom, LocalDateTime deadlineDateTo){
        return goalDBRepo.findAllByAuthorIdAndIsDoneAndDeadlineBetween(authorId, isDone, deadlineDateFrom, deadlineDateTo);
    }
    protected List<Goal> findAllByAuthorIdAndIsDoneAndCreatedAtBetween(long authorId, boolean isDone, LocalDateTime createdAtDateFrom, LocalDateTime createdAtDateTo){
        return goalDBRepo.findAllByAuthorIdAndIsDoneAndCreatedAtBetween(authorId, isDone, createdAtDateFrom, createdAtDateTo);
    }
    protected List<Goal> findAllByAuthorIdAndDeadlineBetweenAndCreatedAtBetween(long authorId, LocalDateTime deadlineDateFrom, LocalDateTime deadlineDateTo, LocalDateTime createdAtDateFrom, LocalDateTime createdAtDateTo){
        return goalDBRepo.findAllByAuthorIdAndDeadlineBetweenAndCreatedAtBetween(authorId, deadlineDateFrom, deadlineDateTo, createdAtDateFrom, createdAtDateTo);
    }
    protected List<Goal> findAllByIsDoneAndDeadlineBetweenAndCreatedAtBetween(boolean isDone, LocalDateTime deadlineDateFrom, LocalDateTime deadlineDateTo, LocalDateTime createdAtDateFrom, LocalDateTime createdAtDateTo){
        return goalDBRepo.findAllByIsDoneAndDeadlineBetweenAndCreatedAtBetween(isDone, deadlineDateFrom, deadlineDateTo, createdAtDateFrom, createdAtDateTo);
    }
    protected List<Goal> findAllByAuthorIdAndIsDoneAndDeadlineBetweenAndCreatedAtBetween(long authorId, boolean isDone, LocalDateTime deadlineDateFrom, LocalDateTime deadlineDateTo, LocalDateTime createdAtDateFrom, LocalDateTime createdAtDateTo){
        return goalDBRepo.findAllByAuthorIdAndIsDoneAndDeadlineBetweenAndCreatedAtBetween(authorId, isDone, deadlineDateFrom, deadlineDateTo, createdAtDateFrom, createdAtDateTo);
    }
    protected Goal save(Goal goal) {
        return goalDBRepo.save(goal);
    }
}
