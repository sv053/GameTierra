package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.User;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRepositoryTest {

    @Test
    void findUser_NotFound() {
        UserRepository repository = new UserRepository();
        assertEquals(Optional.empty(), repository.findById(7));
    }

    @Test
    void findUser_Success() {
        User user = new User(8, "thunder", null, null);
        List<User> users = List.of(user);
        UserRepository repository = new UserRepository();
        repository.create(users);
        Optional<User> foundUser = repository.findById(user.getId());

        assertEquals(Optional.of(user), foundUser);
    }

    @Test
    void createAll() {
        List<User> users = List.of(
                new User(6, "addedUser1", null, null),
                new User(7, "addedUser2", null, null));
        UserRepository repository = new UserRepository();
        repository.create(users);

        assertEquals(users.size(), repository.find().size());
        assertTrue(repository.find().containsAll(users));
    }
}

