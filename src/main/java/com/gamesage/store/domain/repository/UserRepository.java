package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.User;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@org.springframework.stereotype.Repository
public class UserRepository implements Repository<User, Integer> {

    private final List<User> users;
    private final Map<Integer, User> allUsersById;

    public UserRepository() {
        users = new ArrayList<>();
        allUsersById = new HashMap<>();
    }

    @Override
    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(allUsersById.get(id));
    }

    @Override
    public List<User> getAll() {
        return users;
    }

    public void createUser(User user){
        users.add(user);
        addUsersToMap(List.of(user));
    }

    @Override
    public void createAll(List<User> usersToAdd) {
        users.addAll(usersToAdd);
        addUsersToMap(usersToAdd);
    }

    private void addUsersToMap(List<User> usersToAdd) {
        Map<Integer, User> mapForNewUsers = usersToAdd.stream()
                .collect(
                        Collectors.toMap(User::getId, Function.identity(),
                                (oldValue, newValue) -> (newValue)));
        allUsersById.putAll(mapForNewUsers);
    }
}

