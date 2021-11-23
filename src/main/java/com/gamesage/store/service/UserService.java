package com.gamesage.store.service;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.IRepository;
import com.gamesage.store.domain.repository.UserRepository;

//@Service
public class UserService {
    private final IRepository<User, Integer> repository;

    public UserService() {
        repository = new UserRepository();
    }

    public UserService(IRepository<User, Integer> repository) {
        this.repository = repository;
    }

    public User findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException(String.format("User with login %s is not found", id)));
    }
}

