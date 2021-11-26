package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class UserRepository implements CustomRepository<User, Integer> {

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

