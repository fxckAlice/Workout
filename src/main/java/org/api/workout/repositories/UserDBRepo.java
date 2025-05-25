package org.api.workout.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.api.workout.enteties.user.User;

public interface UserDBRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
    boolean existsByUsername(String username);
}
