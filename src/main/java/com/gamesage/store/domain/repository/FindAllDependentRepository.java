package com.gamesage.store.domain.repository;

import java.util.List;

public interface FindAllDependentRepository<T, K> extends Repository<T, K> {

    List<T> findAllDependent(K ownerId);
}

