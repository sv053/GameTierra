package com.gamesage.store.domain.repository;

public interface FindById<T> {
    default T findBy(final Integer key) {
        throw new UnsupportedOperationException();
    }
}
