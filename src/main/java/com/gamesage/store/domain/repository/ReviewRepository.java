package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.GameReview;

import java.util.List;

public interface ReviewRepository<T, K> extends Repository<T, K> {

    List<T> findByUserId(Integer userId);

    List<GameReview> findByGameId(Integer gameId);
}

