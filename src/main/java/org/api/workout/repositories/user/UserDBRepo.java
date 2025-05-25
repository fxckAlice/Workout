package org.api.workout.repositories.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.api.workout.enteties.user.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDBRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
    boolean existsByUsername(String username);
}
