package com.gamesage.store.service;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.CustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private CustomRepository<User, Integer> customRepository;

    public UserService(CustomRepository<User, Integer> customRepository) {
        this.customRepository = customRepository;
    }

    public User findById(Integer id) {
        return customRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("User with login %s is not found", id)));
    }
}

