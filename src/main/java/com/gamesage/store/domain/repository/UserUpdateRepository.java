package com.gamesage.store.domain.repository;

public interface UserUpdateRepository<User, Integer> extends Repository<User, Integer> {

    User updateUserBalance(User user);
}

