package com.gamesage.store.domain.repository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository<T, K> extends CreateOneRepository<T, K> {

    List<T> findByUserId(Integer userId, Integer page, Integer size);

    List<T> findByGameId(Integer gameId, Integer page, Integer size);

    Optional<T> findById(K id);

    T updateReview(T review);

    void deleteAll();
}

