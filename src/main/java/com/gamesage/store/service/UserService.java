package com.gamesage.store.service;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.UpdateRepository;
import com.gamesage.store.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UpdateRepository<User, Integer> repository;

    public UserService(@Qualifier("dbUserRepository") UpdateRepository<User, Integer> repository) {
        this.repository = repository;
    }

    public User findById(int id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public User createOne(User userToAdd) {
        return repository.createOne(userToAdd);
    }

    public User updateBalance(User userToUpdate) {
        return repository.updateColumn(userToUpdate).orElseThrow(() -> new EntityNotFoundException(userToUpdate.getId()));
    }
}

