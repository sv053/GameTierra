package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.GameReview;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository<Review, Integer> extends CreateOneRepository<Review, Integer> {

    List<Review> findByUserId(Integer userId, Integer page, Integer size);

    GameReview findByGameId(Integer gameId, Integer page, Integer size);

    Optional<Review> findById(Integer id);

    Review updateReview(Review review);
}

