package com.gamesage.store.domain.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, K> {
   List<T> createAll(List<T> items);

   Optional<T> findBy(K key);
}

