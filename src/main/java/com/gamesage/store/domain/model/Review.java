package com.gamesage.store.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Review {

    private final Integer userId;
    private final Integer gameId;
    private final LocalDateTime dateTime;
    private Integer id;
    private Integer mark;
    private String opinion;

    public Review(Integer userId, Integer gameId) {
        this.dateTime = LocalDateTime.now();
        this.userId = userId;
        this.gameId = gameId;
    }

    public Review(Integer id, Integer userId, Integer gameId, LocalDateTime localDateTime) {
        this.id = id;
        this.dateTime = localDateTime;
        this.userId = userId;
        this.gameId = gameId;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getGameId() {
        return gameId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return userId.equals(review.userId) && gameId.equals(review.gameId) && Objects.equals(id, review.id) && Objects.equals(mark, review.mark) && Objects.equals(opinion, review.opinion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, gameId, id, mark, opinion);
    }

    @Override
    public String toString() {
        return "Review{" +
                "userId=" + userId +
                ", gameId=" + gameId +
                ", id=" + id +
                ", mark=" + mark +
                ", opinion='" + opinion + '\'' +
                '}';
    }
}

