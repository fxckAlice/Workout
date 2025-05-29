package org.api.workout.security;


import org.api.workout.entities.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassHash();
        this.authorities = user.getRole();
    }

    public CustomUserDetails(long l, String username, String pwd, List<? extends GrantedAuthority> authorities) {
        this.id = l;
        this.username = username;
        this.password = pwd;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
    public long getId() {
        return id;
    }

}
