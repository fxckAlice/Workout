package org.api.workout.enteties.goals;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.api.workout.enteties.user.User;

import java.time.LocalDateTime;


@Entity
@Table(name = "goals")
@Getter
@Setter
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    private String text;
    private boolean isDone;
    @Column(name = "deadline", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime deadline;
    @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime createdAt;

    public Goal() {}
    public Goal(User author, String text) {
        this.author = author;
        this.text = text;
        this.createdAt = LocalDateTime.now();
    }
    @PrePersist
    public void prePersist() {
        this.setCreatedAt(LocalDateTime.now());
    }
}
