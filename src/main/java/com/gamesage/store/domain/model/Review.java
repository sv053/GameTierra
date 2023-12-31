package com.gamesage.store.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;

public class Review {

    private final Integer userId;
    private final Integer gameId;
    private LocalDateTime dateTime;
    private Integer id;
    private Boolean rating;
    private String opinion;

    public Review(Integer userId, Integer gameId) {
        this.dateTime = LocalDateTime.now();
        this.userId = userId;
        this.gameId = gameId;
    }

    public Review(Integer id, Integer userId, Integer gameId, LocalDateTime localDateTime) {
        this.id = id;
        this.userId = userId;
        this.gameId = gameId;
        this.dateTime = localDateTime;
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Review(@JsonProperty("id") Integer id,
                  @JsonProperty("user_id") Integer userId,
                  @JsonProperty("game_id") Integer gameId,
                  @JsonProperty("rating") Boolean rating,
                  @JsonProperty("opinion") String opinion,
                  @JsonProperty("review_dateTime") LocalDateTime dateTime) {
        this.id = id;
        this.userId = userId;
        this.gameId = gameId;
        this.rating = rating;
        this.opinion = opinion;
        this.dateTime = dateTime;
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

    public Boolean getRating() {
        return rating;
    }

    public void setRating(Boolean rating) {
        this.rating = rating;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return userId.equals(review.userId) && gameId.equals(review.gameId)
                && Objects.equals(id, review.id) && Objects.equals(rating, review.rating)
                && Objects.equals(opinion, review.opinion) && Objects.equals(dateTime, review.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, gameId, id, rating, opinion, dateTime);
    }

    @Override
    public String toString() {
        return "Review{" +
                "userId=" + userId +
                ", gameId=" + gameId +
                ", id=" + id +
                ", rating=" + rating +
                ", opinion='" + opinion + '\'' +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}

