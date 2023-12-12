package com.gamesage.store.domain.model;

import java.util.List;

public class GameReview {

    private final Integer gameId;

    private final List<Review> reviews;
    private Double avgRating;
    private Double avgPositiveRating;
    private Integer positiveRatingCount;
    private Integer negativeRatingCount;

    public GameReview(Integer gameId, List<Review> reviews) {
        this.gameId = gameId;
        this.reviews = reviews;
    }

    public Double getAvgPositiveRating() {
        return avgPositiveRating;
    }

    public void setAvgPositiveRating() {
        this.avgPositiveRating = calculateAvgPositiveUserExperience();
    }

    public Integer getPositiveRatingCount() {
        return positiveRatingCount;
    }

    public void setPositiveRatingCount() {
        this.positiveRatingCount = calculatePositiveUserExperience();
    }

    public Integer getNegativeRatingCount() {
        return negativeRatingCount;
    }

    public void setNegativeRatingCount() {
        this.negativeRatingCount = calculateNegativeUserExperience();
    }

    public Integer getGameId() {
        return gameId;
    }

    public List<Review> getReviews() {
        return reviews;
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

    public Integer calculatePositiveUserExperience() {
        return reviews.stream()
                .mapToInt(Review::getRating)
                .sum();
    }

    public Double calculateAvgPositiveUserExperience() {
        return calculateAvgUserExperience() / reviews.size() * 100;
    }

    public Integer calculateNegativeUserExperience() {
        return reviews.size() - calculatePositiveUserExperience();
    }
}

