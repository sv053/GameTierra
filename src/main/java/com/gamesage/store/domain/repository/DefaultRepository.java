package com.gamesage.store.domain.repository;

import java.util.List;

public interface DefaultRepository<T1, T2> {
   T1 read(List<T2> items);
}
