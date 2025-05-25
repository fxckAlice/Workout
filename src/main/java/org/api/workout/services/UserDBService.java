package org.api.workout.services;

import org.api.workout.enteties.user.User;
import org.api.workout.repositories.UserDBRepo;
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
