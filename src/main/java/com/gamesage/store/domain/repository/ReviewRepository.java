package com.gamesage.store.domain.repository;

import java.util.List;

public interface ReviewRepository<T, K> extends Repository<T, K> {

    List<T> findByUserId(Integer userId);

    List<T> findByGameId(Integer gameId);

    T updateReview(T review);

    void deleteAll();
}

