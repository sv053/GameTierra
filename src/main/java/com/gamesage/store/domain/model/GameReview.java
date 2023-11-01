package com.gamesage.store.domain.model;

import java.util.List;
import java.util.stream.IntStream;

public class GameReview {

    private final Integer gameId;

    private Integer id;
    private List<Review> reviews;
    private Double avgUserExperience;

    public GameReview(Integer gameId) {
        this.gameId = gameId;
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

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Double getAvgUserExperience() {
        return avgUserExperience;
    }

    public void setAvgUserExperience() {
        this.avgUserExperience = calculateAvgUserExperience();
    }

    private Double calculateAvgUserExperience() {
        return reviews.stream()
                .flatMapToInt(review -> IntStream.of(review.getMark()))
                .average()
                .orElse(0);
    }
}

