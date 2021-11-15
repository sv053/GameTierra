package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.User;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserRepository implements Repository<User, String> {
    private final List<User> users;
    private final Map<String, User> allUsersByLogin;

    public UserRepository() {
        this.users = new ArrayList<>();
        this.allUsersByLogin = new HashMap<>();
    }

    public List<User> getUsers() {
        return this.users;
    }

    @Override
    public List<User> createAll(final List<User> usersToAdd) {
        this.users.addAll(usersToAdd);
        this.addUsersToMap(usersToAdd);
        return usersToAdd;
    }

    private Map<String, User> addUsersToMap(final List<User> usersToAdd) {
        final Map<String, User> mapForNewUsers = usersToAdd.stream()
                .collect(
                        Collectors.toMap(User::getLogin, Function.identity(),
                                (oldValue, newValue) -> (newValue)));
        this.allUsersByLogin.putAll(mapForNewUsers);
        return mapForNewUsers;
    }

    @Override
    public Optional<User> findBy(final String login) {
        return Optional.ofNullable(this.allUsersByLogin.get(login));
    }
}

