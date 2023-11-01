package com.gamesage.store.domain.repository;

import java.util.List;

public interface GameReviewsRepository<T, K, U> extends Repository<T, K> {

    List<T> findByUserId(Integer userId);

    U findByGameId(Integer gameId);
}

