package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.User;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public User createOne(User user){
        User userWithId = assignId(user);
        users.add(userWithId);
        allUsersById.put(userWithId.getId(), userWithId);
        return userWithId;
    }

    @Override
    public List<User> create(List<User> usersToAdd){
        List<User> usersToAddWithId = assignIdToAll(usersToAdd);
        users.addAll(usersToAddWithId);
        addUsersToMap(usersToAddWithId);
        return usersToAddWithId;
    }

    private List<User> assignIdToAll(List<User> usersToAddId) {
        usersToAddId.forEach(this::assignId);
        return usersToAddId;
    }

    private User assignId(User user) {
        user.setId(idCounter++);
        return user;
    }

    private void addUsersToMap(List<User> usersToAdd) {
        Map<Integer, User> mapForNewUsers = usersToAdd.stream()
                .collect(
                        Collectors.toMap(User::getId, Function.identity(),
                                (oldValue, newValue) -> (newValue)));
        allUsersById.putAll(mapForNewUsers);
    }
}

