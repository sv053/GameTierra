package com.gamesage.store.domain.repository;

public interface RatingRepository {

    Integer addGame(Integer gameId);

    Integer updateRating(Integer gameId, Boolean rating);

    Integer addRating(Integer gameId, Boolean rating);

    Integer findRating(Integer gameId);

    Integer findRatingsAmount(Integer gameId);

}

