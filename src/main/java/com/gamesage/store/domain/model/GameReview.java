package com.gamesage.store.domain.model;

import java.util.List;

public class GameReview {

    private final Integer gameId;

    private static Integer id = 0;
    private List<Review> reviews;
    private Double avgRating;

    public GameReview(Integer gameId, List<Review> reviews) {
        id++;
        this.gameId = gameId;
        this.reviews = reviews;
    }

    public Integer getGameId() {
        return gameId;
    }

    public Integer getId() {
        return id;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public GameReview setAvgRating() {
        this.avgRating = calculateAvgUserExperience();
        return this;
    }

    private Double calculateAvgUserExperience() {
        return reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
    }
}

