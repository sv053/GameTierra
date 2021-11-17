package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRepositoryTest {

    @Test
    void findBy_NotFound() {
        UserRepository repository = new UserRepository();
        assertEquals(Optional.empty(), repository.findById(7));
    }

    @Test
    void findById_Success() {
        UserRepository repository = new UserRepository();
        List<User> users = new ArrayList<>();
        User user = new User(8, "thunder", null, null);
        users.add(user);
        repository.createAll(users);
        Optional<User> foundUser = repository.findById(user.getId());

        assertTrue(foundUser.isPresent());
        if (foundUser.isPresent()) {
            assertEquals(user, foundUser.get());
        }
    }

    @Test
    void createAll() {
        UserRepository repository = new UserRepository();

        List<User> users = Arrays.asList(
                new User(6, "addedUser1", null, null),
                new User(7, "addedUser2", null, null)
        );
        repository.createAll(users);

        assertEquals(users.size(), repository.getAll().size());
        assertTrue(repository.getAll().containsAll(users));
    }
}

