package org.api.workout.services.workout;

import org.api.workout.controllers.exceptions.workout.WorkoutNotFoundException;
import org.api.workout.entities.workout.Workout;
import org.api.workout.entities.workout.WorkoutType;
import org.api.workout.repositories.workout.WorkoutDBRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("unused")
@Service
public class WorkoutDBService {
    private final WorkoutDBRepo workoutDBRepo;
    protected WorkoutDBService(WorkoutDBRepo workoutDBRepo) {
        this.workoutDBRepo = workoutDBRepo;
    }
    protected List<Workout> findAll(){
        return workoutDBRepo.findAll();
    }
    protected List<Workout> findAllByAuthorId(Long authorId){
        return workoutDBRepo.findAllByAuthorId(authorId);
    }
    protected List<Workout> findAllByAuthorIdAndIsDone(Long authorId, boolean isDone){
        return workoutDBRepo.findAllByAuthorIdAndIsDone(authorId, isDone);
    }
    protected List<Workout> findAllByAuthorIdAndType(Long authorId, WorkoutType type){
        return workoutDBRepo.findAllByAuthorIdAndType(authorId, type);
    }
    protected List<Workout> findAllByAuthorIdAndDateBetween(Long authorId, LocalDateTime dateFrom, LocalDateTime dateTo){
        return workoutDBRepo.findAllByAuthorIdAndDateBetween(authorId, dateFrom, dateTo);
    }
    protected List<Workout> findAllByAuthorIdAndDateBetweenAndType(Long authorId, LocalDateTime dateFrom, LocalDateTime dateTo, WorkoutType type){
        return workoutDBRepo.findAllByAuthorIdAndDateBetweenAndType(authorId, dateFrom, dateTo, type);
    }
    protected List<Workout> findAllByAuthorIdAndDateBetweenAndIsDone(Long authorId, LocalDateTime dateFrom, LocalDateTime dateTo, boolean isDone){
        return workoutDBRepo.findAllByAuthorIdAndDateBetweenAndIsDone(authorId, dateFrom, dateTo, isDone);
    }
    protected List<Workout> findAllByAuthorIdAndDateBetweenAndTypeAndIsDone(Long authorId, LocalDateTime dateFrom, LocalDateTime dateTo, WorkoutType type, boolean isDone){
        return workoutDBRepo.findAllByAuthorIdAndDateBetweenAndTypeAndIsDone(authorId, dateFrom, dateTo, type, isDone);
    }
    protected List<Workout> findAllByDateBetweenAndType(LocalDateTime dateFrom, LocalDateTime dateTo, WorkoutType type){
        return workoutDBRepo.findAllByDateBetweenAndType(dateFrom, dateTo, type);
    }
    protected List<Workout> findAllByDateBetweenAndIsDone(LocalDateTime dateFrom, LocalDateTime dateTo, boolean isDone){
        return workoutDBRepo.findAllByDateBetweenAndIsDone(dateFrom, dateTo, isDone);
    }
    protected List<Workout> findAllByDateBetweenAndTypeAndIsDone(LocalDateTime dateFrom, LocalDateTime dateTo, WorkoutType type, boolean isDone){
        return workoutDBRepo.findAllByDateBetweenAndTypeAndIsDone(dateFrom, dateTo, type, isDone);
    }
    protected List<Workout> findAllByAuthorIdAndIsDoneAndType(Long authorId, boolean isDone, WorkoutType type){
        return workoutDBRepo.findAllByAuthorIdAndIsDoneAndType(authorId, isDone, type);
    }
    protected List<Workout> findAllByTypeAndIsDone(WorkoutType type, boolean isDone){
        return workoutDBRepo.findAllByTypeAndIsDone(type, isDone);
    }
    protected List<Workout> findAllByType(WorkoutType type){
        return workoutDBRepo.findAllByType(type);
    }
    protected List<Workout> findAllByIsDone(boolean isDone){
        return workoutDBRepo.findAllByIsDone(isDone);
    }
    protected List<Workout> findAllByDateBetween(LocalDateTime dateFrom, LocalDateTime dateTo){
        return workoutDBRepo.findAllByDateBetween(dateFrom, dateTo);
    }
    protected Workout findById(long id) {
        return workoutDBRepo.findById(id).orElseThrow(() -> new WorkoutNotFoundException("Workout with id: " + id + " not found."));
    }
    protected int countAllByAuthorId(long authorId) {
        return workoutDBRepo.countAllByAuthorId(authorId);
    }
    protected int countByAuthorIdAndIsDone(long authorId, boolean isDone) {
        return workoutDBRepo.countByAuthorIdAndIsDone(authorId, isDone);
    }
    protected int countByAuthorIdAndType(long authorId, WorkoutType type) {
        return workoutDBRepo.countByAuthorIdAndType(authorId, type);
    }
    protected int countByAuthorIdAndTypeAndIsDone(long authorId, WorkoutType type, boolean isDone) {
        return workoutDBRepo.countByAuthorIdAndTypeAndIsDone(authorId, type, isDone);
    }
    protected Workout findTopByAuthorIdAndIsDoneOrderByDateDesc(long authorId) {
        return workoutDBRepo.findTopByAuthorIdAndIsDoneOrderByDateDesc(authorId, true).orElseThrow(() -> new WorkoutNotFoundException("Workout not found for authorId: " + authorId + " and isDone: " + true + "."));
    }
    protected Workout findTopByAuthorIdAndIsDoneAndDateGreaterThanEqualOrderByDateAsc(long authorId) {
        return workoutDBRepo.findTopByAuthorIdAndIsDoneAndDateGreaterThanEqualOrderByDateAsc(authorId, false, LocalDateTime.now()).orElseThrow(() -> new WorkoutNotFoundException("Workout not found for authorId: " + authorId + "."));
    }
    protected void deleteById(long id) {
        workoutDBRepo.deleteById(id);
    }
    protected Workout save(Workout workout) {
        return workoutDBRepo.save(workout);
    }
}
