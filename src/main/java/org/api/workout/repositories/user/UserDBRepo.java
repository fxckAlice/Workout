package org.api.workout.repositories.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.api.workout.enteties.user.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDBRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
