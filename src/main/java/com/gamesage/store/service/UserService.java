package com.gamesage.store.service;


import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.Repository;

public class UserService {

    private final Repository<User, Integer> repository;

    public UserService(final Repository<User, Integer> repository) {
        this.repository = repository;
    }

    public User findById(final Integer id) {
        return this.repository.findById(id).orElseThrow(
                () -> new IllegalArgumentException(String.format("User with login " + id + " is not found")));
    }
}

