package com.gamesage.store.domain.repository;

import java.util.List;
import java.util.Optional;

public interface CustomRepository<T, K> {

    Optional<T> findById(K id);

    List<T> getAll();

    void createAll(List<T> items);
}

