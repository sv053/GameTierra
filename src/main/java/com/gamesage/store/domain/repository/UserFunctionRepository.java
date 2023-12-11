package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.User;

import java.util.Optional;

public interface UserFunctionRepository extends Repository<User, Integer> {

    User updateUserBalance(User user);

    Optional<User> findByLogin(String login);

    void deleteAll();
}

