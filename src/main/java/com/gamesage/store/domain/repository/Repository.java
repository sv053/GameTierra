package com.gamesage.store.domain.repository;

import java.util.List;

public interface Repository<T> {
   List<T> createAll(List<T> items);

   T findBy(int fieldName);
}

