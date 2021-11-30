package com.gamesage.store.domain.repository;


import java.util.List;
import java.util.Optional;

public interface Repository<T, K> {

    Optional<T> findById(K id);
    List<T> findAll();
    List<T> create(List<T> items);
    T createOne(T item);
}

