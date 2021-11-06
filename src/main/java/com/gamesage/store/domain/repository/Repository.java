package com.gamesage.store.domain.repository;

import java.util.List;

public interface Repository<T1> {
   List<T1> createAll(List<T1> items);
}

