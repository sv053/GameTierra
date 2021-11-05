package com.gamesage.store.domain.repository;

import java.util.List;

public interface Repository<T1> {
   List<T1> findAll();

   Repository updateAll(List<T1> items);

   T1 findById(int id);
}

