package com.gamesage.store.domain.repository;

import java.util.Optional;

public interface UpdateRepository<T, K> extends Repository<T, K> {

    Optional<T> update(T item);
    Optional<T> updateColumn(T item);
}
