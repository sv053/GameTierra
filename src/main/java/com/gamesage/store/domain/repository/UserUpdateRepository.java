package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.User;

import java.util.Optional;

public interface UserUpdateRepository extends Repository<User, Integer> {

    User updateUserBalance(User user);

    Optional<User> findByLogin(String login);
}

