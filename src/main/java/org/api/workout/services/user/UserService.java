package org.api.workout.services.user;

import org.api.workout.controllers.dto.GoalDTO;
import org.api.workout.controllers.dto.RegisterRequestDTO;
import org.api.workout.controllers.dto.UserDTO;
import org.api.workout.controllers.dto.WorkoutDTO;
import org.api.workout.enteties.user.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SuppressWarnings("")
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
            throw new RuntimeException("Already exists");
        }
        User user = new User(requestDTO.username(), passwordEncoder.encode(requestDTO.password()));
        return userDBService.save(user);
    }
    public User login(RegisterRequestDTO requestDTO) {
        User user = userDBService.findByUsername(requestDTO.username());
        if (!passwordEncoder.matches(requestDTO.password(), user.getPassHash())) {
            throw new RuntimeException("Invalid password");
        }
        userDBService.save(user);
        return user;
    }
    @Transactional
    public UserDTO getUserDTOById(long id) {
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
}
