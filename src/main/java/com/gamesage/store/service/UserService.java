package com.gamesage.store.service;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final Repository<User, Integer> repository;

    public UserService(Repository<User, Integer> repository) {
        this.repository = repository;
    }

    public User findById(Integer id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("User with login %s is not found", id)));
    }

    public List<User> create(List<User> usersToAdd){
        return repository.create(usersToAdd);
    }

    public User createOne(User userToAdd){
        return repository.createOne(userToAdd);
    }

    public List<User> findAll(){
        return repository.findAll();
    }
}

