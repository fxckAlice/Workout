package org.api.workout.entities.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.api.workout.entities.goals.Goal;
import org.api.workout.entities.workout.Workout;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String passHash;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "author_id"))
    @Enumerated(EnumType.STRING)
    private Set<UserRoles> role = new HashSet<>();

    @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime createdAt;

    @Column(name = "last_login", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime lastLogin;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Workout> workouts = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Goal> goals = new ArrayList<>();
    public User() {}
    public User(String username, String passHash) {
        this.username = username;
        this.passHash = passHash;
        this.role.add(UserRoles.ROLE_USER);
        this.createdAt = LocalDateTime.now();
        this.lastLogin = LocalDateTime.now();
    }
    public User(long id, String username, String passHash) {
        this.id = id;
        this.username = username;
        this.passHash = passHash;
        this.role.add(UserRoles.ROLE_USER);
        this.createdAt = LocalDateTime.now();
        this.lastLogin = LocalDateTime.now();
    }
    @PrePersist
    public void prePersist() {
        this.setCreatedAt(LocalDateTime.now());
        this.setLastLogin(LocalDateTime.now());
    }
    @PostLoad
    public void postLoad() {
        this.setLastLogin(LocalDateTime.now());
    }
}
