package org.api.workout.entities.workout;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.api.workout.entities.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "workouts")
@Getter
@Setter
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    @Column(name = "date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime date;
    private boolean isDone;
    @Enumerated(EnumType.STRING)
    private WorkoutType type;
    private int duration;
    @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime createdAt;

    public Workout() {}
    public Workout(User author, WorkoutType type, LocalDateTime date) {
        this.author = author;
        this.type = type;
        this.date = date;
        this.createdAt = LocalDateTime.now();
        this.isDone = false;
    }
    @PrePersist
    public void prePersist() {
        this.setCreatedAt(LocalDateTime.now());
    }
}
