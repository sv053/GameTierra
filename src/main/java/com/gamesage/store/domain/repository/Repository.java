package com.gamesage.store.domain.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, K> {

   List<T> getAll();

   void createAll(List<T> items);

   Optional<T> findById(K id);
}

