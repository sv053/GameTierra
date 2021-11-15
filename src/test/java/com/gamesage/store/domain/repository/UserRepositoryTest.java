package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRepositoryTest {

    @Test
    void findBy_NotFound() {
        UserRepository repository = new UserRepository();
        assertEquals(Optional.empty(), repository.findBy("lark"));
    }

    @Test
    void findBy_Success() {
        UserRepository repository = new UserRepository();
        final List<User> users = Arrays.asList(
                new User(
                        "thunder",
                        new Tier("first", 75.),
                        null));
        repository.createAll(users);
        final User user = users.get(users.size() - 1);
        final Optional<User> foundUser = repository.findBy(user.getLogin());

        assertTrue(foundUser.isPresent());
        if (foundUser.isPresent()) {
            assertEquals(user, foundUser.get());
        }
    }

    @Test
    void createAll() {
        UserRepository repository = new UserRepository();

        List<User> users = Arrays.asList(
                new User("addedUser1", null, null),
                new User("addedUser2", null, null)
        );
        repository.createAll(users);

        assertEquals(users.size(), repository.getUsers().size());

        for (final User user : users) {
            assertTrue(repository.getUsers().contains(user));
        }
    }
}

