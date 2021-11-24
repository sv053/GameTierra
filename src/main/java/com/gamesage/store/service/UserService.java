package com.gamesage.store.service;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.Repository;
import com.gamesage.store.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final Repository<User, Integer> repository;

    public UserService() {
        repository = new UserRepository();
    }

    public UserService(Repository<User, Integer> repository) {
        this.repository = repository;
    }

    public User findById(Integer id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("User with login %s is not found", id)));
    }
}

