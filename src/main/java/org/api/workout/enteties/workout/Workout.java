package org.api.workout.enteties.workout;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.api.workout.enteties.user.User;

import java.time.OffsetDateTime;

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
    private OffsetDateTime date;
    private boolean isDone;
    @Enumerated(EnumType.STRING)
    private WorkoutType type;
    private int duration;
    @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdAt;

    public Workout() {}
    public Workout(User author, WorkoutType type, OffsetDateTime date) {
        this.author = author;
        this.type = type;
        this.date = date;
        this.createdAt = OffsetDateTime.now();
    }
    public Workout(long authorId, WorkoutType type, OffsetDateTime date) {
        this.type = type;
        this.date = date;
        this.createdAt = OffsetDateTime.now();
    }
    @PrePersist
    public void prePersist() {
        this.setCreatedAt(OffsetDateTime.from(java.time.LocalDateTime.now()));
    }
}
