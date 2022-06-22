package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.User;

import java.util.Optional;

public interface FindByLoginRepository extends UserUpdateRepository<User, Integer> {

    Optional<User> findByLogin(String login);
}

