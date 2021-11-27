package com.gamesage.store.service;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    private Repository<User, Integer> repository;

    public UserService(Repository<User, Integer> repository) {
        this.repository = repository;
    }

    public User findById(Integer id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("User with login %s is not found", id)));
    }
}

