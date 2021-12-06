package com.gamesage.store.service;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final Repository<User, Integer> repository;

    public UserService(Repository<User, Integer> repository) {
        this.repository = repository;
    }

//    public Optional<User> findById(Integer id) {
//        return repository.findById(id);
//    }

    public User findById(int id){
        return repository.findById(id).orElseThrow(
                () -> new IllegalArgumentException(
                        String.format("User with id %s not found", id)));
    }

    public User createOne(User userToAdd) {
        return repository.createOne(userToAdd);
    }

    public List<User> findAll() {
        return repository.findAll();
    }
}

