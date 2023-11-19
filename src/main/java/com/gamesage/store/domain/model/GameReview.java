package com.gamesage.store.domain.model;

import java.util.List;
import java.util.stream.IntStream;

public class GameReview {

    private final Integer gameId;

    private Integer id;
    private List<Review> reviews;
    private Double avgRating;

    public GameReview(Integer gameId, Integer id, List<Review> reviews, Double avgRating) {
        this.gameId = gameId;
        this.id = id;
        this.reviews = reviews;
        this.avgRating = avgRating;
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

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating() {
        this.avgRating = calculateAvgUserExperience();
    }

    private Double calculateAvgUserExperience() {
        return reviews.stream()
                .flatMapToInt(review -> IntStream.of(review.getRating()))
                .average()
                .orElse(0);
    }
}

