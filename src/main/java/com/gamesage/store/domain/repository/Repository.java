package com.gamesage.store.domain.repository;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface Repository<T, K> {

   Optional<T> findById(K id);
   List<T> getAll();
   void createAll(List<T> items);
}

