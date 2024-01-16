package com.gamesage.store.domain.model;

import java.util.List;

public class GameReview {

    private final Integer gameId;
    private final List<Review> reviews;
    private Double avgRating;
    private Double avgPositiveRating;
    private Integer positiveRatingCount;
    private Integer negativeRatingCount;

    public GameReview(Integer gameId,
                      List<Review> reviews,
                      Double avgRating,
                      Integer positiveRating,
                      Integer negativeRating) {
        this.gameId = gameId;
        this.reviews = reviews;
        this.avgRating = avgRating;
        this.positiveRatingCount = positiveRating;
        this.negativeRatingCount = negativeRating;
    }

    public Double getAvgPositiveRating() {
        return avgPositiveRating;
    }

    public Integer getPositiveRatingCount() {
        return positiveRatingCount;
    }

    public Integer getNegativeRatingCount() {
        return negativeRatingCount;
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

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public void setAvgPositiveRating(Double avgPositiveRating) {
        this.avgPositiveRating = avgPositiveRating;
    }

    public void setPositiveRatingCount(Integer positiveRatingCount) {
        this.positiveRatingCount = positiveRatingCount;
    }

    public void setNegativeRatingCount(Integer negativeRatingCount) {
        this.negativeRatingCount = negativeRatingCount;
    }

    @Override
    public String toString() {
        return "GameReview{" +
                "gameId=" + gameId +
                ", reviews=" + reviews +
                ", avgRating=" + avgRating +
                ", avgPositiveRating=" + avgPositiveRating +
                ", positiveRatingCount=" + positiveRatingCount +
                ", negativeRatingCount=" + negativeRatingCount +
                '}';
    }
}

