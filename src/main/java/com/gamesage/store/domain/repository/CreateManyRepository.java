package com.gamesage.store.domain.repository;

import java.sql.SQLException;
import java.util.List;

public interface CreateManyRepository<T, K> extends Repository<T, K> {
    List<T> create(List<T> items) throws SQLException;
}

