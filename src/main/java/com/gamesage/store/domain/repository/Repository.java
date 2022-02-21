package com.gamesage.store.domain.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, K> extends CreateOneRepository<T, K>{

    Optional<T> findById(K id);
    List<T> findAll();
}

