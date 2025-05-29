package org.api.workout.repositories.workout;

import org.api.workout.entities.workout.Workout;
import org.api.workout.entities.workout.WorkoutType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("all")
@Repository
public interface WorkoutDBRepo extends JpaRepository<Workout, Long> {


    List<Workout> findAll();
    List<Workout> findAllByAuthorId(Long authorId);
    List<Workout> findAllByAuthorIdAndIsDone(Long authorId, boolean isDone);
    List<Workout> findAllByAuthorIdAndType(Long authorId, WorkoutType type);
    List<Workout> findAllByAuthorIdAndDateBetween(Long authorId, LocalDateTime dateFrom, LocalDateTime dateTo);
    List<Workout> findAllByAuthorIdAndDateBetweenAndType(Long authorId, LocalDateTime dateFrom, LocalDateTime dateTo, WorkoutType type);
    List<Workout> findAllByAuthorIdAndDateBetweenAndIsDone(Long authorId, LocalDateTime dateFrom, LocalDateTime dateTo, boolean isDone);
    List<Workout> findAllByAuthorIdAndDateBetweenAndTypeAndIsDone(Long authorId, LocalDateTime dateFrom, LocalDateTime dateTo, WorkoutType type, boolean isDone);
    List<Workout> findAllByDateBetweenAndType(LocalDateTime dateFrom, LocalDateTime dateTo, WorkoutType type);
    List<Workout> findAllByDateBetweenAndIsDone(LocalDateTime dateFrom, LocalDateTime dateTo, boolean isDone);
    List<Workout> findAllByDateBetweenAndTypeAndIsDone(LocalDateTime dateFrom, LocalDateTime dateTo, WorkoutType type, boolean isDone);
    List<Workout> findAllByAuthorIdAndIsDoneAndType(Long authorId, boolean isDone, WorkoutType type);
    List<Workout> findAllByTypeAndIsDone(WorkoutType type, boolean isDone);
    List<Workout> findAllByType(WorkoutType type);
    List<Workout> findAllByIsDone(boolean isDone);
    List<Workout> findAllByDateBetween(LocalDateTime dateFrom, LocalDateTime dateTo);
    Optional<Workout> findById(long id);
    @SuppressWarnings("all")
    int countAllByAuthorId(long authorId);
    int countByAuthorIdAndIsDone(long authorId, boolean isDone);
    int countByAuthorIdAndType(long authorId, WorkoutType type);
    int countByAuthorIdAndTypeAndIsDone(long authorId, WorkoutType type, boolean isDone);
    Optional<Workout> findTopByAuthorIdAndIsDoneOrderByDateDesc(long authorId, boolean isDone);
    Optional<Workout> findTopByAuthorIdAndIsDoneAndDateGreaterThanEqualOrderByDateAsc(long authorId, boolean isDone, LocalDateTime date);
    void deleteById(long id);
}
