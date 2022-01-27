package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.Game;

import java.util.List;

public interface DbRepository<T, K> extends Repository<T, K>{
    List<T> retrieveAll ();
    List<T> insertAll(List<Game> items);
}
