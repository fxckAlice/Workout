package org.api.workout.security;

import org.api.workout.enteties.user.User;
import org.api.workout.repositories.user.UserDBRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class DBUserDetailsService implements UserDetailsService {
    private final UserDBRepo userDBRepo;

    public DBUserDetailsService(UserDBRepo userDBRepo) {
        this.userDBRepo = userDBRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        User user = userDBRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(user);
    }
}
