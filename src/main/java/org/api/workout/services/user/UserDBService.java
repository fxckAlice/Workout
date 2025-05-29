package org.api.workout.services.user;

import org.api.workout.exceptions.user.UserNotFoundException;
import org.api.workout.entities.user.User;
import org.api.workout.repositories.user.UserDBRepo;
import org.springframework.stereotype.Service;

@Service
public class UserDBService{
    private final UserDBRepo userDBRepo;
    protected UserDBService(UserDBRepo userDBRepo) {
        this.userDBRepo = userDBRepo;
    }
    protected boolean existsByUsername(String username) {
        return userDBRepo.existsByUsername(username);
    }
    protected User save(User user) {
        return userDBRepo.save(user);
    }
    protected User findByUsername(String username) {
        return userDBRepo.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
    }
    protected User findById(long id) {
        return userDBRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
