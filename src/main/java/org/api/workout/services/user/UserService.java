package org.api.workout.services.user;

import org.api.workout.dto.goal.GoalDTO;
import org.api.workout.dto.user.RegisterRequestDTO;
import org.api.workout.dto.user.UserDTO;
import org.api.workout.dto.workout.WorkoutDTO;
import org.api.workout.exceptions.user.UserAlreadyExistsException;
import org.api.workout.exceptions.workout.AccessForbiddenException;
import org.api.workout.entities.user.User;
import org.api.workout.security.CustomUserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserDBService userDBService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserDBService userDBService, PasswordEncoder passwordEncoder) {
        this.userDBService = userDBService;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterRequestDTO requestDTO) {
        if (userDBService.existsByUsername(requestDTO.username())) {
            throw new UserAlreadyExistsException("User already exists");
        }
        User user = new User(requestDTO.username(), passwordEncoder.encode(requestDTO.password()));
        return userDBService.save(user);
    }
    @SuppressWarnings("all")
    public User login(RegisterRequestDTO requestDTO) {
        User user = userDBService.findByUsername(requestDTO.username());
        if (!passwordEncoder.matches(requestDTO.password(), user.getPassHash())) {
            throw new IllegalArgumentException("Invalid password");
        }
        userDBService.save(user);
        return user;
    }
    @SuppressWarnings("all")
    @Transactional
    public UserDTO getUserDTOById(long id, CustomUserDetails userDetails) {
        if (id != userDetails.getId() && userDetails.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessForbiddenException("You don't have access to this user.");
        }
        User user = userDBService.findById(id);

        user.getWorkouts().size();
        List<WorkoutDTO> workouts = user.getWorkouts().stream().map(
                w -> new WorkoutDTO(w.getId(), w.getAuthor().getId(), w.getDate(), w.isDone(), w.getType(), w.getDuration(), w.getCreatedAt())
        ).toList();

        user.getGoals().size();
        List<GoalDTO> goals = user.getGoals().stream().map(
                g -> new GoalDTO(g.getId(), g.getAuthor().getId(), g.getText(), g.isDone(), g.getDeadline(), g.getCreatedAt())
        ).toList();
        return new UserDTO(user.getId(), user.getUsername(), user.getCreatedAt(), workouts, goals);
    }
    public User findById(long id) {
        return userDBService.findById(id);
    }
}
