package com.gamesage.store.service;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.Repository;
import com.gamesage.store.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class UserService {

    private final Repository<User, Integer> repository;

    public UserService(@Qualifier("dbUserRepository") Repository<User, Integer> repository) {
        this.repository = repository;
    }

    public User findById(int id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public User createOne(User userToAdd) throws SQLException {
        return repository.createOne(userToAdd);
    }
}

