package com.checkers.web.domain;

import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Calendar;

@Entity
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

    public Long getScoreId() {
        return scoreId;
    }

    public void setScoreId(Long scoreId) {
        this.scoreId = scoreId;
    }

    public String getUser() {
        return user.toString();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Winner getWinner() {
        return winner;
    }

    public void setWinner(Winner winner) {
        this.winner = winner;
    }

    public int getAdvantage() {
        return advantage;
    }

    public void setAdvantage(int advantage) {
        this.advantage = advantage;
    }

    public String getCreatedAt() {
        return createdAt.getTime().toString();
    }

    public void setCreatedAt(Calendar createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Score)) return false;

        Score score = (Score) o;

        if (getAdvantage() != score.getAdvantage()) return false;
        if (getScoreId() != null ? !getScoreId().equals(score.getScoreId()) : score.getScoreId() != null) return false;
        if (getUser() != null ? !getUser().equals(score.getUser()) : score.getUser() != null) return false;
        if (getWinner() != score.getWinner()) return false;
        return getCreatedAt() != null ? getCreatedAt().equals(score.getCreatedAt()) : score.getCreatedAt() == null;
    }

    @Override
    public int hashCode() {
        int result = getScoreId() != null ? getScoreId().hashCode() : 0;
        result = 31 * result + (getUser() != null ? getUser().hashCode() : 0);
        result = 31 * result + (getWinner() != null ? getWinner().hashCode() : 0);
        result = 31 * result + getAdvantage();
        result = 31 * result + (getCreatedAt() != null ? getCreatedAt().hashCode() : 0);
        return result;
    }
}
