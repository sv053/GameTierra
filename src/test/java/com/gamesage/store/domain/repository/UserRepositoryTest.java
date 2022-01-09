package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.User;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserRepositoryTest {

    @Test
    void findUser_NotFound() {
        UserRepository repository = new UserRepository();
        assertEquals(Optional.empty(), repository.findById(7));
    }

    @Test
    void findUser_Success() {
        User user = new User(8, "thunder", null, null);
        UserRepository repository = new UserRepository();
        repository.createOne(user);
        Optional<User> foundUser = repository.findById(user.getId());

        assertEquals(Optional.of(user), foundUser);
    }
}

