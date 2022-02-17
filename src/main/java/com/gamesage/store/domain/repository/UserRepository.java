package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.User;

import java.util.*;

@org.springframework.stereotype.Repository
public class UserRepository implements Repository<User, Integer> {

    private final List<User> users;
    private final Map<Integer, User> allUsersById;
    private int idCounter = 1;

    public UserRepository() {
        users = new ArrayList<>();
        allUsersById = new HashMap<>();
    }

    @Override
    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(allUsersById.get(id));
    }

    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public User createOne(User user) {
        User userWithId = assignId(user);
        users.add(userWithId);
        allUsersById.put(userWithId.getId(), userWithId);
        return userWithId;
    }

    @Override
    public int update(User item) {
        return 0;
    }

    private User assignId(User user) {
        user.setId(idCounter++);
        return user;
    }
}

