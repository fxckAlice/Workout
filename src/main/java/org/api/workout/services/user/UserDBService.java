package org.api.workout.services.user;

import org.api.workout.enteties.user.User;
import org.api.workout.repositories.user.UserDBRepo;
import org.springframework.stereotype.Service;

@Service
public class UserDBService{
    private final UserDBRepo userDBRepo;
    public UserDBService(UserDBRepo userDBRepo) {
        this.userDBRepo = userDBRepo;
    }
    public boolean existsByUsername(String username) {
        return userDBRepo.existsByUsername(username);
    }
    public User save(User user) {
        return userDBRepo.save(user);
    }
    public User findByUsername(String username) {
        return userDBRepo.findByUsername(username);
    }
}
