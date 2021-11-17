package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.User;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserRepository implements Repository<User, Integer> {

    private final List<User> users;
    private final Map<Integer, User> allUsersById;

    public UserRepository() {
        this.users = new ArrayList<>();
        this.allUsersById = new HashMap<>();
    }

    @Override
    public List<User> getAll() {
        return this.users;
    }

    @Override
    public void createAll(final List<User> usersToAdd) {
        this.users.addAll(usersToAdd);
        this.addUsersToMap(usersToAdd);
    }

    private void addUsersToMap(final List<User> usersToAdd) {
        final Map<Integer, User> mapForNewUsers = usersToAdd.stream()
                .collect(
                        Collectors.toMap(User::getId, Function.identity(),
                                (oldValue, newValue) -> (newValue)));
        this.allUsersById.putAll(mapForNewUsers);
    }

    @Override
    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(this.allUsersById.get(id));
    }
}

