package org.api.workout.repositories.goal;

import org.api.workout.entities.goals.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface GoalDBRepo extends JpaRepository<Goal, Long> {
    @SuppressWarnings("all")
    List<Goal> findAll();
    List<Goal> findAllByAuthorId(long authorId);
    @SuppressWarnings("all")
    List<Goal> findAllByIsDone(boolean isDone);
    List<Goal> findAllByDeadlineBetween(LocalDateTime deadlineDateFrom, LocalDateTime deadlineDateTo);
    List<Goal> findAllByCreatedAtBetween(LocalDateTime createdAtDateFrom, LocalDateTime createdAtDateTo);
    @SuppressWarnings("all")
    List<Goal> findAllByAuthorIdAndIsDone(long authorId, boolean isDone);
    List<Goal> findAllByAuthorIdAndDeadlineBetween(long authorId, LocalDateTime deadlineDateFrom, LocalDateTime deadlineDateTo);
    List<Goal> findAllByAuthorIdAndCreatedAtBetween(long authorId, LocalDateTime createdAtDateFrom, LocalDateTime createdAtDateTo);
    @SuppressWarnings("all")
    List<Goal> findAllByIsDoneAndDeadlineBetween(boolean isDone, LocalDateTime deadlineDateFrom, LocalDateTime deadlineDateTo);
    @SuppressWarnings("all")
    List<Goal> findAllByIsDoneAndCreatedAtBetween(boolean isDone, LocalDateTime createdAtDateFrom, LocalDateTime createdAtDateTo);
    List<Goal> findAllByDeadlineBetweenAndCreatedAtBetween(LocalDateTime deadlineDateFrom, LocalDateTime deadlineDateTo, LocalDateTime createdAtDateFrom, LocalDateTime createdAtDateTo);
    @SuppressWarnings("all")
    List<Goal> findAllByAuthorIdAndIsDoneAndDeadlineBetween(long authorId, boolean isDone, LocalDateTime deadlineDateFrom, LocalDateTime deadlineDateTo);
    @SuppressWarnings("all")
    List<Goal> findAllByAuthorIdAndIsDoneAndCreatedAtBetween(long authorId, boolean isDone, LocalDateTime createdAtDateFrom, LocalDateTime createdAtDateTo);
    List<Goal> findAllByAuthorIdAndDeadlineBetweenAndCreatedAtBetween(long authorId, LocalDateTime deadlineDateFrom, LocalDateTime deadlineDateTo, LocalDateTime createdAtDateFrom, LocalDateTime createdAtDateTo);
    @SuppressWarnings("all")
    List<Goal> findAllByIsDoneAndDeadlineBetweenAndCreatedAtBetween(boolean isDone, LocalDateTime deadlineDateFrom, LocalDateTime deadlineDateTo, LocalDateTime createdAtDateFrom, LocalDateTime createdAtDateTo);
    @SuppressWarnings("all")
    List<Goal> findAllByAuthorIdAndIsDoneAndDeadlineBetweenAndCreatedAtBetween(long authorId, boolean isDone, LocalDateTime deadlineDateFrom, LocalDateTime deadlineDateTo, LocalDateTime createdAtDateFrom, LocalDateTime createdAtDateTo);
    Optional<Goal> findById(long id);
    int countAllByAuthorId(long authorId);
    @SuppressWarnings("all")
    int countByAuthorIdAndIsDone(long authorId, boolean isDone);
    Optional<Goal> findTopByAuthorIdAndDeadlineGreaterThanEqualOrderByDeadlineAsc(long authorId, LocalDateTime deadline);
    void deleteById(long id);
}
