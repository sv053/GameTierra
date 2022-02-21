package com.gamesage.store.domain.repository;

public interface CreateOneRepository<T, K> {
    T createOne(T item);
}

