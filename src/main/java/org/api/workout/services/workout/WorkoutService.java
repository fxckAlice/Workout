package org.api.workout.services.workout;

import org.api.workout.controllers.dto.NewWorkoutDTO;
import org.api.workout.controllers.dto.WorkoutDTO;
import org.api.workout.controllers.dto.WorkoutFilterDTO;
import org.api.workout.controllers.exceptions.workout.AccessForbiddenException;
import org.api.workout.entities.workout.Workout;
import org.api.workout.entities.workout.WorkoutType;
import org.api.workout.security.CustomUserDetails;
import org.api.workout.services.user.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class WorkoutService {
    private final WorkoutDBService workoutDBService;
    private final UserService userService;

    public WorkoutService(WorkoutDBService workoutDBService, UserService userService) {
        this.workoutDBService = workoutDBService;
        this.userService = userService;
    }

    public List<WorkoutDTO> findWorkoutsByFilter(WorkoutFilterDTO filter) {
        Long authorId = null;
        if (filter.authorId() != null) {
            try {
                authorId = Long.parseLong(filter.authorId());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid authorId: " + filter.authorId());
            }
        }

        Boolean isDone = null;
        if (filter.isDone() != null) {
            isDone = Boolean.parseBoolean(filter.isDone());
        }

        LocalDateTime dateFrom = filter.dateFrom();
        LocalDateTime dateTo = filter.dateTo();

        WorkoutType type = null;
        if (filter.type() != null) {
            type = filter.type();
        }

        List<Workout> workouts;

        if (authorId == null && isDone == null && dateFrom == null && dateTo == null && type == null) {
            workouts = workoutDBService.findAll();
        } else if (authorId != null && isDone == null && dateFrom == null && dateTo == null && type == null) {
            workouts = workoutDBService.findAllByAuthorId(authorId);
        } else if (authorId != null && isDone != null && dateFrom == null && dateTo == null && type == null) {
            workouts = workoutDBService.findAllByAuthorIdAndIsDone(authorId, isDone);
        } else if (authorId != null && isDone == null && dateFrom == null && dateTo == null) {
            workouts = workoutDBService.findAllByAuthorIdAndType(authorId, type);
        } else if (authorId != null && dateFrom != null && dateTo != null && isDone == null && type == null) {
            workouts = workoutDBService.findAllByAuthorIdAndDateBetween(authorId, dateFrom, dateTo);
        } else if (authorId != null && dateFrom != null && dateTo != null && type != null && isDone == null) {
            workouts = workoutDBService.findAllByAuthorIdAndDateBetweenAndType(authorId, dateFrom, dateTo, type);
        } else if (authorId != null && dateFrom != null && dateTo != null && type == null) {
            workouts = workoutDBService.findAllByAuthorIdAndDateBetweenAndIsDone(authorId, dateFrom, dateTo, isDone);
        } else if (authorId != null && dateFrom != null && dateTo != null) {
            workouts = workoutDBService.findAllByAuthorIdAndDateBetweenAndTypeAndIsDone(authorId, dateFrom, dateTo, type, isDone);
        } else if (dateFrom != null && dateTo != null && type != null && isDone == null) {
            workouts = workoutDBService.findAllByDateBetweenAndType(dateFrom, dateTo, type);
        } else if (dateFrom != null && dateTo != null && isDone != null && type == null) {
            workouts = workoutDBService.findAllByDateBetweenAndIsDone(dateFrom, dateTo, isDone);
        } else if (dateFrom != null && dateTo != null && type != null) {
            workouts = workoutDBService.findAllByDateBetweenAndTypeAndIsDone(dateFrom, dateTo, type, isDone);
        } else if (authorId != null && isDone != null && type != null && dateFrom == null && dateTo == null) {
            workouts = workoutDBService.findAllByAuthorIdAndIsDoneAndType(authorId, isDone, type);
        } else if (type != null && isDone != null && authorId == null && dateFrom == null && dateTo == null) {
            workouts = workoutDBService.findAllByTypeAndIsDone(type, isDone);
        } else if (type != null && isDone == null && authorId == null && dateFrom == null && dateTo == null) {
            workouts = workoutDBService.findAllByType(type);
        } else if (isDone != null && type == null && authorId == null && dateFrom == null && dateTo == null) {
            workouts = workoutDBService.findAllByIsDone(isDone);
        } else if (dateFrom != null && dateTo != null) {
            workouts = workoutDBService.findAllByDateBetween(dateFrom, dateTo);
        } else {
            workouts = Collections.emptyList();
        }

        return workouts.stream()
                .map(w -> new WorkoutDTO(
                        w.getId(),
                        w.getAuthor().getId(),
                        w.getDate(),
                        w.isDone(),
                        w.getType(),
                        w.getDuration(),
                        w.getCreatedAt()
                ))
                .toList();
    }
    public Workout newWorkout(NewWorkoutDTO newWorkoutDTO, Long authorId) {
        Workout workout = new Workout(
                userService.findById(authorId),
                newWorkoutDTO.type(),
                newWorkoutDTO.date()
        );
        if (newWorkoutDTO.duration() != null) {
            workout.setDuration(newWorkoutDTO.duration());
        }
        return workoutDBService.save(workout);
    }
    public WorkoutDTO findWorkoutById(long id, CustomUserDetails userDetails) {
        Workout workout = workoutDBService.findById(id);
        if (workout.getAuthor().getId() != userDetails.getId() && userDetails.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessForbiddenException("You don't have access to this workout.");
        }
        return new WorkoutDTO(
                workout.getId(),
                workout.getAuthor().getId(),
                workout.getDate(),
                workout.isDone(),
                workout.getType(),
                workout.getDuration(),
                workout.getCreatedAt()
        );
    }
    public WorkoutDTO updateWorkout(long id, WorkoutDTO workoutDTO, CustomUserDetails userDetails) {
        Workout workout = workoutDBService.findById(id);
        if (workout.getAuthor().getId() != userDetails.getId() &&  userDetails.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessForbiddenException("You don't have access to this workout.");
        }
        workout.setDate(workoutDTO.date());
        workout.setDone(workoutDTO.isDone());
        workout.setType(workoutDTO.type());
        if (workoutDTO.duration() != null) {
            workout.setDuration(workoutDTO.duration());
        }
        workout = workoutDBService.save(workout);
        return new WorkoutDTO(
                workout.getId(),
                workout.getAuthor().getId(),
                workout.getDate(),
                workout.isDone(),
                workout.getType(),
                workout.getDuration(),
                workout.getCreatedAt()
        );
    }
    public void deleteWorkout(long id, CustomUserDetails userDetails) {
        Workout workout = workoutDBService.findById(id);
        if (workout.getAuthor().getId() != userDetails.getId() &&  userDetails.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessForbiddenException("You don't have access to this workout.");
        }
        workoutDBService.deleteById(id);
    }
}
