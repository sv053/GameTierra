package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.User;

public interface UserUpdateRepository extends Repository<User, Integer> {

    User update(User user);

    User updateUserBalance(User user);
}

