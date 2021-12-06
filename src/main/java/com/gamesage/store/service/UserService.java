package com.gamesage.store.service;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.Repository;
import com.gamesage.store.exception.userexception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final Repository<User, Integer> repository;

    public UserService(Repository<User, Integer> repository) {
        this.repository = repository;
    }

    public User findById(int id){
        return repository.findById(id).orElseThrow(
                () -> new UserNotFoundException(
                        String.format("User with id %s not found", id)));
    }

    public User createOne(User userToAdd) {
        return repository.createOne(userToAdd);
    }

    public List<User> findAll() {
        return repository.findAll();
    }
}

