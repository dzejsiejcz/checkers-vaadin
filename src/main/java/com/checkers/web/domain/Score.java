package com.checkers.web.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Getter
@Setter
@Table(name = "scores")
@AllArgsConstructor
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id",  unique = true, nullable = false)
    private Long scoreId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Winner winner;

    private int advantage;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    Calendar createdAt;

    public Score() {
    }

    public Score(User user, Winner winner, int advantage) {
        this.user = user;
        this.winner = winner;
        this.advantage = advantage;
    }
}
