package com.gamesage.store.domain.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Repository<T, K> {

    Optional<T> findById(K id);

    List<T> findAll();

    T createOne(T item) throws SQLException;
}

