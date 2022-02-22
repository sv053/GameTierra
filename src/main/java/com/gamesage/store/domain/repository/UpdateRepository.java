package com.gamesage.store.domain.repository;

public interface UpdateRepository<T, K> extends Repository<T, K> {

    T update(T item);
    T updateUserBalanceColumn(T item);
}

