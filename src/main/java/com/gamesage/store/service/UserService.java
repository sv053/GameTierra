package com.gamesage.store.service;


import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.Repository;

import java.util.Optional;

public class UserService {
    private final Repository<User, String> repository;

    public UserService(Repository<User, String> repository) {
        this.repository = repository;
    }

    public User findByLogin(final String login) {
        Optional<User> foundUser = this.repository.findBy(login);
        if (foundUser.isEmpty()) throw new IllegalArgumentException("User with login " + login + " is not found");
        return foundUser.get();
    }
}

