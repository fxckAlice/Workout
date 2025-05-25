package org.api.workout.enteties.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String passHash;
    @Enumerated(EnumType.STRING)
    private UserRoles role;
    @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdAt;
    @Column(name = "last_login", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime lastLogin;

    public User() {}
    public User(String username, String passHash) {
        this.username = username;
        this.passHash = passHash;
        this.role = UserRoles.USER;
        this.createdAt = OffsetDateTime.now();
        this.lastLogin = OffsetDateTime.now();
    }
    @PrePersist
    public void prePersist() {
        this.setCreatedAt(OffsetDateTime.from(LocalDateTime.now()));
        this.setLastLogin(OffsetDateTime.from(LocalDateTime.now()));
    }
    @PostLoad
    public void postLoad() {
        this.setLastLogin(OffsetDateTime.from(LocalDateTime.now()));
    }
}
