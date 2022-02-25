package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.User;

public interface UpdateRepository extends Repository<User, Integer> {

    User update(User user);

    User updateUserBalance(User user);
}

